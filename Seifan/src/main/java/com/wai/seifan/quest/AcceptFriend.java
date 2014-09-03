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

public class AcceptFriend extends Questable implements Url{
	private static final String URL_FRIEND = URL + "/friend";
	private static final String URL_FRIEND_APPLY = URL_FRIEND + "/appli";
	
	public AcceptFriend() {
	}
	
	@Override
	public void execute() throws Exception {
		while (true) {
			logger.info("SCANNING TO ACCEPT NEW FRIEND");
			Response r = this.getResponse("http://chada.seifan.shopgautho.com/friend/");
			Element addElement = Jsoup.parse(r.getResponseBody()).select("a[href^=/friend/appli?ah]").first();
			if (addElement != null) {
				String urlFriend = addElement.attr("href");
				r = this.getResponse(URL + urlFriend);
				
				Elements formElements = Jsoup.parse(r.getResponseBody()).select("form");
				for (Element formElement : formElements) {
					String addFriendUrl = URL + formElement.attr("action");
					this.getResponse(addFriendUrl, "data%5Bresult%5D=ok");
					logger.info("You ("+this.username+") just added " + addFriendUrl + " as new friend.");
				}
			} else {
				logger.info("No one want to make friend.");
			}
			
			Thread.sleep(2000);
		}
	}

	public long getPotentialPoint() throws Exception {
		Response response = this.getResponse(URL_USER_HOME);		
		Elements notElements = Jsoup.parse(response.getResponseBody()).select("ul.verticalOverlayMenu > li > a");
		if (notElements.size() > 0) {
			for (Element notElement : notElements) {
				if (StringUtils.contains(notElement.attr("href"), "/friend/")) {
					return Long.parseLong(StringUtils.split(StringUtils.split(notElement.text(), "+")[1], ")")[0]);
				}
			}
		}
		return 0L;
	}

	@Override
	public void doQuest(String level) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public QuestInfo getQuestInfo(Response response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}