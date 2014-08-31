package com.wai.seifan.quest;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ning.http.client.Response;
import com.wai.seifan.common.Const;
import com.wai.seifan.common.Quest;
import com.wai.seifan.common.Url;
import com.wai.seifan.dto.QuestInfo;
import com.wai.seifan.util.Utils;

public class QuestDragonTower extends Quest implements Url{
	private static final String URL_QUEST_DO = URL + "/event/chada_raid_quest/execute";
	private static final String URL_QUEST_DO_NOT = URL_QUEST_DO + "/not";
	private static final String URL_QUEST_DO_INDEX =  URL + "/event/chada_raid_quest/index";
	
	private static final String URL_BOSS_HOME =  URL + "/event/chada_raid_quest/boss";
	
	public QuestDragonTower() {
		this.isUsedMana = true;
		this.isUsedManaSmall = true;
		this.isUsedManaFullLocked = true;
		this.isUsedManaFullOpened = false;
	}
	
	public QuestDragonTower(boolean isUsedMana, boolean isUsedManaSmall, boolean isUsedManaFullLocked, boolean isUsedManaFullOpened) {
		this.isUsedMana = isUsedMana;
		this.isUsedManaSmall = isUsedManaSmall;
		this.isUsedManaFullLocked = isUsedManaFullLocked;
		this.isUsedManaFullOpened = isUsedManaFullOpened;
	}
	
	@Override
	public void execute() throws Exception {
		while (true) {
			this.usePotentialPoint(1, 0, 0);
			
			// call to index to know that if there is any bigboss or someone need help
			Response indexResponse = this.getResponse(URL_QUEST_DO_INDEX);
			Element bossDiv = Jsoup.parse(indexResponse.getResponseBody()).select("div.bgImage").first();
			
			boolean isDoBigBoss = config.getBoolean("quest.dragon.do_big_boss");
			boolean isHelp = config.getBoolean("quest.dragon.help");
			
			// attack big boss
			if (isDoBigBoss) {
				Element bigBossElement = bossDiv.select("> div > div.gradiationGray > a").first();
				if (bigBossElement != null) {
					Response bossDetailResponse = this.getResponse(URL + bigBossElement.attr("href"));
					Element doBossElement = Jsoup.parse(bossDetailResponse.getResponseBody()).getElementById("raidBoss").select("> div.gradiationGray > span > a").first();
					if (doBossElement != null) {
						this.getResponse("http://chada.seifan.shopgautho.com/swf_touch/201404202060/raid_boss_battle/");
						this.getResponse("http://chada.seifan.shopgautho.com/raid_boss/battle_result/");
						logger.info("DO A BIG BOSS");
						continue;
					}
				}
			}
			
			// find someone need help
			if (isHelp) {
				Element someoneNeedHelpElement = bossDiv.select("> div > div.gradiationGray > a").last();
				if (someoneNeedHelpElement != null) {
					Response needHelpResponse = this.getResponse(URL + "/raid_boss/other/cdrq");
					Elements userElements = Jsoup.parse(needHelpResponse.getResponseBody()).select("a[href^=/user/details/] > span");
					for (Element userElement : userElements) {
						String username = userElement.text();
						if (Const.ACCOUNT_MAIN.contains(username)) {
							Response bossDetailResponse = this.getResponse(URL + userElement.parent().select("> div > a").first().attr("href"));
							Element doBossElement = Jsoup.parse(bossDetailResponse.getResponseBody()).getElementById("raidBoss").select("> div.gradiationGray > span > a").first();
							if (doBossElement != null) {
								this.getResponse("http://chada.seifan.shopgautho.com/swf_touch/201404202060/raid_boss_battle/");
								this.getResponse("http://chada.seifan.shopgautho.com/raid_boss/battle_result/");
								logger.info("HELP " + username + " TO ATTACK A BIG BOSS");
								continue;
							}
						}
					}
				}
			}
					
			// send request to home of adventure quest
			Response homeResponse = this.getResponse(URL_QUEST_DO_NOT);
			
			QuestInfo info = this.getQuestInfo(homeResponse);
			if (info.getCompletePercent() == 100L) {
				if (info.getNo() % 10 == 0) {
					// boss
					Response bossHomeResponse = this.getResponse(URL_BOSS_HOME);
					String urlBoss = Jsoup.parse(bossHomeResponse.getResponseBody()).select("div.gradiationGray > span > a").first().attr("href");
					this.getResponse(URL + urlBoss);
					this.getResponse(StringUtils.replace(URL + urlBoss, "swf_touch/201404202060/boss_battle_result", "event/chada_raid_quest/result"));;
					logger.info("DO A BOSS IN EACH 10 LEVEL");
					continue;
				}
				// go to next level
				this.getResponse(URL_QUEST_DO_INDEX);
				logger.info("GO TO THE NEXT LEVEL");
				continue;
			} else {
				// if does not enough mana, wait for some minutes
				if (info.getWaitTime() > 0) {
					if (isUsedManaFullOpened || isUsedManaFullLocked) {
						if (info.getExpWithMana(info.getManaTotal()) <= info.getExpToLevelUp()) {
							if (isUsedManaFullLocked) {
								if (this.useManaFullLocked()) continue;
							}
							if (isUsedManaFullOpened) {
								if (this.useManaFullOpened()) continue;
							}
						}
					}
					
					if (isUsedManaSmall) {
						if (info.getExpWithMana(info.getManaHad() + Const.MANA_SMALL_QUALITY) <= info.getExpToLevelUp()) {
							if (this.useManaSmall()) continue;
						}
					}
					
					this.autoAddMana();
//					logger.info("WAIT	# wait in " + info.getWaitTime() + " minutes");
//					Thread.sleep(info.getWaitTime()*60*1000);
					continue;
				}
				
				this.doQuest(null);
				continue;
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
		
		// get reputationPoint
		String reputation = mainDiv.select("> span").get(2).text();
		Long reputationPoint = Long.parseLong(reputation);
		info.setReputationPoint(reputationPoint);
		
		// get no and title
		String title = mainDiv.select("> div > div.mapTitle > span").first().text();
		info.setTitle(title);
		String no = StringUtils.split(title)[4];
		info.setNo(Long.parseLong(Utils.removeInvalidChars(no)));
		
		// get percentage of finish
		String percentage = mainDiv.select("> table").first().select("> tbody > tr > td > div > span > span").first().text();
		long completePercent = Long.parseLong(StringUtils.split(percentage, "%")[0]);
		info.setCompletePercent(completePercent);
		
		// get mana cost and exp gain
		Elements costSpans = mainDiv.select("> div > span");
		Long manaCost = Long.parseLong(costSpans.get(costSpans.size() - 3).text())*(-1);
		Long expGain = Long.parseLong(costSpans.get(costSpans.size() - 1).text());
		info.setManaCost(manaCost);
		info.setExpGain(expGain);
		
		// get other information
		Elements questInfoSpans = mainDiv.select("> table").last().select("> tbody > tr > td > div > span");
		String[] manas = StringUtils.split(questInfoSpans.get(1).text(), "/");
		String[] attacks = StringUtils.split(questInfoSpans.get(5).text(), "/");
		String[] exps = StringUtils.split(questInfoSpans.get(7).text(), "/");
		Long manaHad = Long.parseLong(manas[0]);
		Long manaTotal = Long.parseLong(manas[1]);
		Long attackHad = Long.parseLong(attacks[0]);
		Long attackTotal = Long.parseLong(attacks[1]);
		Long expCurrent = Long.parseLong(exps[0]);
		Long expTotal = Long.parseLong(exps[1]);
		info.setManaHad(manaHad);
		info.setManaTotal(manaTotal);
		info.setAttackHad(attackHad);
		info.setAttackTotal(attackTotal);
		info.setExpCurrent(expCurrent);
		info.setExpTotal(expTotal);
		info.setRatioNeeded((double) (expTotal - expCurrent)/manaHad);
		
		logger.info("Level " + info.getTitle() + "("+info.getCompletePercent()+") with " + info.getReputationPoint() + " reputation points.");
		logger.info("Cost :  " + info.getManaCost() + " and gain " + info.getExpGain());
		logger.info("Mana :  " + info.getManaHad() + " / " + info.getManaTotal());
		logger.info("Attack :  " + info.getAttackHad() + " / " + info.getAttackTotal());
		logger.info("Exp :  " + info.getExpCurrent() + " / " + info.getExpTotal());
		
		return info;
	}
}