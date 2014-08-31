package com.wai.seifan.quest;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ning.http.client.Response;
import com.wai.seifan.common.Const;
import com.wai.seifan.common.Questable;
import com.wai.seifan.common.Url;
import com.wai.seifan.dto.QuestInfo;
import com.wai.seifan.util.Utils;

public class QuestMysteryRoad extends Questable implements Url {
	private static final String URL_QUEST_HOME = URL + "/event/chada_mysteryroad_quest/execute/not";
	private static final String URL_QUEST_DO = URL + "/event/chada_mysteryroad_quest/execute/";
	
	public QuestMysteryRoad() {
		this.isUsedMana = true;
		this.isUsedManaSmall = true;
		this.isUsedManaFullLocked = true;
		this.isUsedManaFullOpened = false;
	}
	
	public QuestMysteryRoad(boolean isUsedMana, boolean isUsedManaSmall, boolean isUsedManaFullLocked, boolean isUsedManaFullOpened) {
		this.isUsedMana = isUsedMana;
		this.isUsedManaSmall = isUsedManaSmall;
		this.isUsedManaFullLocked = isUsedManaFullLocked;
		this.isUsedManaFullOpened = isUsedManaFullOpened;
	}
	
	@Override
	public void execute() throws Exception {
		while (true) {
			this.usePotentialPoint(1, 0, 0);
			
			// send request to home of adventure quest
			Response homeResponse = this.getResponse(URL_QUEST_HOME);
			Document homeDocument = Jsoup.parse(homeResponse.getResponseBody());
			
			// in case drop card
			if (StringUtils.contains(homeResponse.getUri().toString(), "swf_touch")) {
				this.getResponse(StringUtils.replace(homeResponse.getUri().toString(), "swf_touch", "swf"));
				this.getResponse(URL_QUEST_DO+"get");
				continue;
			}
			
			// if this is new level, enter it
			if (!StringUtils.contains(homeResponse.getUri().toString(), "/not")) {
				// check if have a battle with other player
				Element battleElement = homeDocument.select("a[href^=/swf_touch/201404202060/encount_battle/cdmq]").first();
				if (battleElement != null) {
					this.getResponse("http://chada.seifan.shopgautho.com/swf_touch/201404202060/encount_battle/cdmq");
					this.getResponse("http://chada.seifan.shopgautho.com/event/chada_mysteryroad_quest/encount_result/cdmq");
					logger.info("Have a battle with other player");
					continue;
				}
				
				// If can find link to go fast, do it
				if (homeDocument.select("a[href^=/event/chada_mysteryroad_quest/execute/difficulty1").first() != null) {
					this.getResponse(URL + "/event/chada_mysteryroad_quest/execute/difficulty1");
					logger.info("Change to fast");
					continue;
				}
				
				this.doQuest(null);
				this.getResponse(URL_QUEST_DO+"get");
				logger.info("Do a quest and go to other level");
				continue;
			} else {
				if (homeDocument.select("form").first() != null) {
					QuestInfo questInfo = this.getQuestInfo(homeResponse);
					logger.info("Level " + questInfo.getNo() + " at step " + questInfo.getStep() + " with " + questInfo.getReputationPoint() + " reputation points.");
					logger.info("Cost :  " + questInfo.getManaCost() + " and gain " + questInfo.getExpGain());
					logger.info("Mana :  " + questInfo.getManaHad() + " / " + questInfo.getManaTotal());
					logger.info("Exp :  " + questInfo.getExpCurrent() + " / " + questInfo.getExpTotal());

					// temporary break at level 11, 21, 31, ....
					if (questInfo.getNo() % 11 == 0) {
						logger.info("NEED TO SWITCH TO FAST MANUALLY.");
//						break;
					}
					// if does not enough mana, wait for some minutes
					if (questInfo.getWaitTime() > 0) {
						if (isUsedManaFullOpened || isUsedManaFullLocked) {
							if (questInfo.getExpWithMana(questInfo.getManaTotal()) <= questInfo.getExpToLevelUp()) {
								if (isUsedManaFullLocked) {
									if (this.useManaFullLocked()) continue;
								}
								if (isUsedManaFullOpened) {
									if (this.useManaFullOpened()) continue;
								}
							}
						}
						
						if (isUsedManaSmall) {
							if (questInfo.getExpWithMana(questInfo.getManaHad() + Const.MANA_SMALL_QUALITY) <= questInfo.getExpToLevelUp()) {
								if (this.useManaSmall()) continue;
							}
						}
						
						this.autoAddMana();
//						logger.info("WAIT	# wait in " + questInfo.getWaitTime() + " minutes");
//						Thread.sleep(questInfo.getWaitTime()*60*1000);
						continue;
					}
					
					this.doQuest(null);
					continue;
				} else {
					// reach the end and need fight a boss
					String urlBoss = homeDocument.select("div.gradiationGray > span > a").first().attr("href");
					this.getResponse(URL + urlBoss);
					this.getResponse("http://chada.seifan.shopgautho.com/event/chada_mysteryroad_quest/execute/back_finished");
					this.getResponse("http://chada.seifan.shopgautho.com/event/chada_mysteryroad_quest/encount_result/cdmq");
					logger.info("Just finish a BOSSSSSSSSSSSSSSSSSSSSSS");
				}
			}
		}
	}

	@Override
	public void doQuest(String level) throws Exception {
		logger.info("Do a quest");
		this.getResponse(URL_QUEST_DO);
	}

	@Override
	public QuestInfo getQuestInfo(Response response) throws Exception {
		QuestInfo info = new QuestInfo();
		
		Document document = Jsoup.parse(response.getResponseBody());
		Element mainDiv = document.getElementById("sectionHeader").nextElementSibling();
		
		// get type "Di nhanh" hay "Di cham"
		
		// get reputationPoint
		String reputation = mainDiv.select("> span").get(2).text();
		Long reputationPoint = Long.parseLong(reputation);
		info.setReputationPoint(reputationPoint);
		
		// get no
		String no = StringUtils.split(mainDiv.select("> div > span").first().text(), ":")[1];
		info.setNo(Long.parseLong(Utils.removeInvalidChars(no)));

		if (mainDiv.select("> span > b").first() != null) {
			String step = StringUtils.split(mainDiv.select("> span > b").first().text())[2];
			info.setStep(Long.parseLong(Utils.removeInvalidChars(step)));
		}
		
		// get mana cost and exp gain
		Elements costSpans = mainDiv.select("form").first().parent().select("> div > span");
		Long manaCost = Long.parseLong(costSpans.get(1).text())*(-1);
		Long expGain = Long.parseLong(costSpans.get(3).text());
		info.setManaCost(manaCost);
		info.setExpGain(expGain);
		
		// get other information
		Elements questInfoSpans = mainDiv.select("> div > table > tbody > tr > td > div > span");
		String[] manas = StringUtils.split(questInfoSpans.get(1).text(), "/");
		String[] exps = StringUtils.split(questInfoSpans.get(4).text(), "/");
		Long manaHave = Long.parseLong(manas[0]);
		Long manaTotal = Long.parseLong(manas[1]);
		Long expCurrent = Long.parseLong(exps[0]);
		Long expTotal = Long.parseLong(exps[1]);
		info.setManaHad(manaHave);
		info.setManaTotal(manaTotal);
		info.setExpCurrent(expCurrent);
		info.setExpTotal(expTotal);
		info.setRatioNeeded((double) (expTotal - expCurrent)/manaHave);
		
		return info;
	}

}
