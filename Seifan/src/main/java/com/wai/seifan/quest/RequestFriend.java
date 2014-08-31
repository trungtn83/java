package com.wai.seifan.quest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.ning.http.client.Response;
import com.wai.seifan.common.Quest;
import com.wai.seifan.common.Url;
import com.wai.seifan.dto.QuestInfo;

public class RequestFriend extends Quest implements Url {
	
	public RequestFriend() {
	}
	
	@Override
	public void execute() throws Exception {
		String[] friends = {"148","150","151","152","153","154","155","156","157","158"};
		
		for (String friendId : friends) {
			Response userDetailResponse = this.getResponse("http://chada.seifan.shopgautho.com/user/details/"+friendId);
			Element deleteElement = Jsoup.parse(userDetailResponse.getResponseBody()).select("a[href^=/friend/delete/]").first();
			if (deleteElement != null) {
				String deleteUrl = URL + deleteElement.attr("href");
				Document confirmDocument = Jsoup.parse(this.getResponse(deleteUrl).getResponseBody());
				
				String confirmUrl = URL + confirmDocument.select("form").first().attr("action");
				String confirm = confirmDocument.getElementById("conform").val();
				String CID = confirmDocument.getElementById("CID").val();
				this.getResponse(confirmUrl, "data%5Bconform%5D="+confirm+"&CID="+CID);
				
				logger.info("Removed " + friendId + " from your friend list");
			}
		}
		
		for (String friendId : friends) {
			Response userDetailResponse = this.getResponse("http://chada.seifan.shopgautho.com/user/details/"+friendId);
			Element addElement = Jsoup.parse(userDetailResponse.getResponseBody()).select("form").first();
			if (addElement != null) {
				String addUrl = URL + addElement.attr("action");
				String appli = addElement.getElementById("appli").val();
				this.getResponse(addUrl, "data%5Bappli%5D="+appli);
				
				logger.info("Added " + friendId + " in your friend list");
			}
		}
		
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