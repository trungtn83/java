package com.wai.seifan.quest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import com.ning.http.client.Response;
import com.wai.seifan.common.Questable;
import com.wai.seifan.common.Url;
import com.wai.seifan.dto.QuestInfo;
import com.wai.seifan.dto.UserInfo;

public class AcceptFriend extends Questable implements Url{
	private static final String URL_FRIEND = URL + "/friend/";
	
	public AcceptFriend(UserInfo userLogin) throws Exception {
		this.login(userLogin);
		this.execute();
	}
	
	@Override
	public void execute() throws Exception {
		logger.info("SCANNING TO ACCEPT NEW FRIEND");
		
		while (true) {
			Response r = this.getResponse(URL_FRIEND);
			Element addElement = Jsoup.parse(r.getResponseBody()).select("a[href^=/friend/appli?ah]").first();
			if (addElement != null) {
				String urlFriend = addElement.attr("href");
				r = this.getResponse(URL + urlFriend);
				
				Element formElement = Jsoup.parse(r.getResponseBody()).select("input#result[value=ok]").first();
				if (formElement == null) break;
				String addFriendUrl = URL + formElement.parent().attr("action");
				this.getResponse(addFriendUrl, "data%5Bresult%5D=ok");
				logger.info("You (" + user.getUsername() + ") just added " + addFriendUrl + " as new friend.");
			} else {
				logger.info("No one want to make friend.");
				break;
			}
		}
		
		this.release();
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