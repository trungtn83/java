package com.wai;

import com.wai.seifan.dto.UserInfo;
import com.wai.seifan.quest.QuestDragonTower;
import com.wai.seifan.quest.QuestDragonTower2;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args ) throws Exception
    {
    	String username = "wai";
    	String password = "hatrung";
    	
//    	Adventure adv = new Adventure();
//    	adv.login(username, password);
//    	adv.execute();
    	
//    	QuestMysteryRoad mysteryRoad = new QuestMysteryRoad(false, false, false, false);
//    	mysteryRoad.login(username, password);
//    	mysteryRoad.execute();
    	
    	QuestDragonTower dragonTower = new QuestDragonTower(false, false, false, false);
    	if (dragonTower.login(new UserInfo(username, password))) {
    		dragonTower.execute();
    	}
    	dragonTower.release();
    		
    	
//    	AcceptFriend add = new AcceptFriend();
//    	add.login(username, password);
//    	add.execute();
    	System.out.println("END");
    }
}
