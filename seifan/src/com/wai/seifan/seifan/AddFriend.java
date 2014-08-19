package com.wai.seifan.seifan;

import com.ning.http.client.Response;
import com.wai.seifan.common.Quest;
import com.wai.seifan.common.Url;
import com.wai.seifan.dto.QuestInfo;

public class AddFriend extends Quest implements Url {
	private String id;
	
	private static final String URL_FRIEND_ADD = URL + "/friend/find_user_level/";

	public AddFriend(String _id) {
		this.id = _id;
	}
	
	@Override
	public void execute() throws Exception {
		this.getResponse(URL_FRIEND_ADD + this.id, "data%5Bappli%5D=1");
	}

	private void doPath(String levelPath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doQuest(String level) throws Exception {
	}

	@Override
	public QuestInfo getQuestInfo(Response response) throws Exception {
		QuestInfo info = new QuestInfo();
		
		return info;
	}
}
