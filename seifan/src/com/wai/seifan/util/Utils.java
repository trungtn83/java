package com.wai.seifan.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.FluentCaseInsensitiveStringsMap;
import com.ning.http.client.Response;
import com.wai.seifan.common.Url;
import com.wai.seifan.dto.QuestInfo;

public class Utils implements Url {
	
	// Lay thong tin ve quest hien tai
	public static QuestInfo getQuestInfo(Response response) throws Exception {
		QuestInfo quest = new QuestInfo();
		
		Document document = Jsoup.parse(response.getResponseBody());
		Element mainDiv = document.getElementById("sectionHeader").nextElementSibling();
		
		// lay thong tin ve so tang
		for (String no : StringUtils.split(mainDiv.select("div.mapTitle > span").first().text())) {
			if (NumberUtils.isNumber(no)) {
				quest.setNo(Long.parseLong(no));
				break;
			}
		}
		// lay diem danh vong
		Long reputationPoint = Long.parseLong(mainDiv.select("> span").get(2).text());
		quest.setReputationPoint(reputationPoint);
		
		// lay diem phan tram hoan thanh
		Elements spans = mainDiv.select("> table").get(0).select("> tbody > tr > td > div > span > span");
		Long completePercent = Long.parseLong(StringUtils.split(spans.get(0).text(), "%")[0]);
		quest.setCompletePercent(completePercent);

		// lay diem mana cost va exp gain
		spans = mainDiv.select("> div").get((completePercent == 100L) ? 6 : 3).select("> span");
		Long manaCost = Long.parseLong(spans.get(1).text())*(-1);
		Long expGain = Long.parseLong(spans.get(3).text());
		quest.setManaCost(manaCost);
		quest.setExpGain(expGain);
		
		// lay thong tin ca nhan con lai
		spans = mainDiv.select("> table").get(1).select("> tbody > tr > td > div > span");
		String[] manas = StringUtils.split(spans.get(1).text(), "/");
		String[] exps = StringUtils.split(spans.get(4).text(), "/");
		Long manaHave = Long.parseLong(manas[0]);
		Long manaTotal = Long.parseLong(manas[1]);
		Long expCurrent = Long.parseLong(exps[0]);
		Long expTotal = Long.parseLong(exps[1]);
		quest.setManaHad(manaHave);
		quest.setManaTotal(manaTotal);
		quest.setExpCurrent(expCurrent);
		quest.setExpTotal(expTotal);
		
		return quest;
	}

//	public static void doResetTowerQuest(AsyncHttpClient client, String session) throws Exception {
//		System.out.println("BAT DAU RESET TOWER");
//		// 1. goi den trang home cua tower quest de tim link
//		//http://chada.seifan.shopgautho.com/event/chada2_tower_quest/index/1
//		Response response = client.preparePost("http://chada.seifan.shopgautho.com/event/chada2_tower_quest/reset_confirm")
//				.setMethod("GET")
//				.setHeaders(getCookieGETHeaders(session))
//				.execute()
//				.get();
//		Document document = Jsoup.parse(response.getResponseBody());
//		if (document.select("div.gradiationGray").last() != null) {
//			String confirmLink = "http://chada.seifan.shopgautho.com/" + document.select("div.gradiationGray").last().select("> a").first().attr("href");
//			// 2. thuc hien reset
//			client.preparePost(confirmLink)
//					.setMethod("GET")
//					.setHeaders(getCookieGETHeaders(session))
//					.execute()
//					.get();
//			System.out.println("BAN VUA RESET SO TANG");
//		}
//	}
	
}
