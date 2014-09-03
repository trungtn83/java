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

public class Adventure extends Questable implements Url {
	private boolean isPath = false;
	private static final String URL_QUEST_HOME = URL + "/quest/";
	private static final String URL_QUEST_DO = URL + "/quest/execute/";
	private static final String URL_QUEST_DROPLIST = URL + "/quest/drop_list/";

	@Override
	public void execute() throws Exception {
		while (true) {
			if (!isRunable) {
				break;
			}
			
			System.out.println("==========================================================================================================================");
			// user all potential point (if have) in basic ratio
			this.usePotentialPoint(-1, -1, -1);
			
			// send request to home of adventure quest
			Response homeResponse = this.getResponse(URL_QUEST_HOME);
			Document homeDocument = Jsoup.parse(homeResponse.getResponseBody());
			
			// check if BOSS
			Element bossFormElement = homeDocument.select("form[action^=/quest/boss/]").first();
			if (bossFormElement != null) {
				// enjoy the boss
				String boss = bossFormElement.attr("action");
				Response bossResponse = this.getResponse(URL + boss);
				// find href to enter the battle
				String battleHref = Jsoup.parse(bossResponse.getResponseBody()).select("div.gradiationGray > span > a").first().attr("href");
				this.getResponse(URL + battleHref);
				this.getResponse(URL + StringUtils.replace(boss, "boss", "result"));
				
				// TODO: Need to check win or lose
				
				logger.info("BOSS	# found a boss and passed him (not sure about prize :) )");
				continue;
			}
			
			// check if PATH
			if (StringUtils.contains(homeResponse.getUri().toString(), "/not")) {
				logger.info("PATH	# found a path");
				String levelPath = StringUtils.split(homeResponse.getUri().toString(), "/")[3];
				
				// in case timeout and continue with next adventure
				Element form = homeDocument.select("form[method=post]").first();
				if (form != null) {
					if (!StringUtils.contains(form.attr("action"), "/not")) {
						// inside path and timeout
						String[] actions = StringUtils.split(form.attr("action"), "/");
						this.doQuest(actions[2]);
						continue;
					} else {
						// have a battle with other player
						if (this.isAttackable(form.parent().parent())) {
							// attack enemy
							String attackUrl = homeDocument.select("div.gradiationGray > span > a").first().attr("href");
							this.getResponse(URL + attackUrl);
							this.getResponse(StringUtils.replace(StringUtils.split(URL + attackUrl, "?")[0], "battle", "result"));
							continue;
						} else {
							// use "diep linh phu" to runaway
							continue;
						}
					}
				} else 

				if (isPath) {
					// check path until go to the end
					this.doPath(levelPath);
					logger.info("PATH	# enjoy the path");
				} else {
					// back from path
					this.getResponse(URL_QUEST_DO + levelPath + "/conf_back");
					this.getResponse(URL_QUEST_DO + levelPath + "/back");
					this.getResponse(URL_QUEST_DROPLIST + levelPath + "/1");
					logger.info("PATH	# hate it and go out");
				}
				continue;
			}
			
			// get current level
			String nextUrl = homeDocument.select("div.gradiationGray > span > a").first().attr("href");
			String level = StringUtils.split(nextUrl, "/")[2];

			// send request to NOT page
			String urlBefore = URL + "/quest/execute/"+level+"/not";
			Response notResponse = this.getResponse(urlBefore);
			String urlAfter = notResponse.getUri().toString();
			if (StringUtils.equals(urlBefore, urlAfter)) {
				// try to find next quest button
				String formAction = Jsoup.parse(notResponse.getResponseBody()).select("form").first().attr("action");
				if (StringUtils.equals(formAction, "/quest/execute/"+level)) {
					logger.info("QUEST	# level : " + level);
					QuestInfo questInfo = this.getQuestInfo(notResponse);
					
					// if does not enough mana, wait for some minutes
					if (questInfo.getWaitTime() > 0) {
						logger.info("WAIT	# wait in " + questInfo.getWaitTime() + " minutes");
						Thread.sleep(questInfo.getWaitTime()*60*1000);
						continue;
					}
					
					// find the max ratio exp in list
					// fix is 1 but will change in future
					int maxIndex = 1;
					
					// find ratio of next quest
					double ratioNeededNextQuest = questInfo.getRatioNeededNext();
					if ((questInfo.getRatioHad() == Const.RATIO_ADVENTURE[maxIndex]) || (ratioNeededNextQuest < Const.RATIO_ADVENTURE[maxIndex])) {
						this.doQuest(level);
					} else {
						this.doQuest(String.valueOf(maxIndex));
					}
				}
			}
		}
		
		// Check response body to switch each case
		// 1. Found a path
		// 		1.1 Still have time to continue
		//		1.2 Timeout, continue with next quest
		// 2. Found an enemy
		// 3. Continue to do next quest
	}

	private void doPath(String levelPath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doQuest(String level) throws Exception {
		this.getResponse(URL_QUEST_DO + level);
		logger.info("QUEST	# did a quest");
	}

	@Override
	public QuestInfo getQuestInfo(Response response) throws Exception {
		QuestInfo info = new QuestInfo();
		
		Document document = Jsoup.parse(response.getResponseBody());
		Element mainDiv = document.getElementById("sectionHeader").nextElementSibling();
		
		// get process progress
		String progress = mainDiv.select("> table > tbody > tr > td > div > span > span").first().text(); 
		info.setCompletePercent(Long.parseLong(StringUtils.remove(progress, '%')));
		
		// get mana cost and exp gain
		Elements spans = mainDiv.select("> table").first().nextElementSibling().nextElementSibling().select("> span");
		long manaCost = Long.parseLong(spans.get(2).text())*(-1);
		long expGain = Long.parseLong(spans.get(5).text());
		info.setManaCost(manaCost);
		info.setExpGain(expGain);
		info.setRatioHad((double) expGain/manaCost);
		
		// get other information
		spans = mainDiv.select("> div > table > tbody > tr > td > div > span");
		String[] manas = StringUtils.split(spans.get(2).text(), "/");
		String[] exps = StringUtils.split(spans.get(6).text(), "/");
		Long manaHave = Long.parseLong(manas[0]);
		Long manaTotal = Long.parseLong(manas[1]);
		Long expCurrent = Long.parseLong(exps[0]);
		Long expTotal = Long.parseLong(exps[1]);
		info.setManaHad(manaHave);
		info.setManaTotal(manaTotal);
		info.setExpCurrent(expCurrent);
		info.setExpTotal(expTotal);
		info.setRatioNeeded((double) (expTotal - expCurrent)/manaHave);

		logger.info("RATIO	# have : " + info.getRatioHad() + " ; needed : " + info.getRatioNeeded());
		logger.info("INFO 	# mana : " + manaHave + "/" + manaTotal + "; exp : " + expCurrent + "/" + expTotal);
		
		return info;
	}

	public boolean isPath() {
		return isPath;
	}

	public void setPath(boolean isPath) {
		this.isPath = isPath;
	}

}
