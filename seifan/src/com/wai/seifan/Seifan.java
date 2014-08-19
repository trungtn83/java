package com.wai.seifan;

import com.wai.seifan.seifan.Adventure;
import com.wai.seifan.seifan.QuestTower3;

public class Seifan {

	public static void main(String[] args) throws Exception {
		String username = args[0];
		String password = "hatrung";
		
//		Adventure adv = new Adventure();
//		adv.login(username, password);
//		adv.setPath(false);
		
		QuestTower3 tower3 = new QuestTower3(true, true, true, false);
		tower3.login(username, password);
		tower3.execute();
		
	}
	
}
