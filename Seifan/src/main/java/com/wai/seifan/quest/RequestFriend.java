package com.wai.seifan.quest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.ning.http.client.Response;
import com.wai.seifan.common.Questable;
import com.wai.seifan.common.Url;
import com.wai.seifan.dto.QuestInfo;

public class RequestFriend extends Questable implements Url {
	
	public RequestFriend() {
	}
	
	@Override
	public void execute() throws Exception {
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