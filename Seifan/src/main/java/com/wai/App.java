package com.wai;

import com.wai.seifan.dto.UserInfo;
import com.wai.seifan.quest.QuestDragonTower;
import com.wai.seifan.quest.QuestTower4;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args ) throws Exception
    {
    	String username = args[0];
    	String password = args[1];
    	
//    	Adventure adv = new Adventure();
//    	adv.login(username, password);
//    	adv.execute();
    	
//    	QuestMysteryRoad mysteryRoad = new QuestMysteryRoad(false, false, false, false);
//    	mysteryRoad.login(username, password);
//    	mysteryRoad.execute();
    	
//    	QuestDragonTower dragonTower = new QuestDragonTower(false, false, false, false);
//    	if (dragonTower.login(new UserInfo(username, password))) {
//    		dragonTower.execute();
//    	}
//    	dragonTower.release();
    	
//    	AcceptFriend add = new AcceptFriend();
//    	add.login(username, password);
//    	add.execute();
    	
    	QuestTower4 tower = new QuestTower4(false, false, false, false);
    	if (tower.login(new UserInfo(username, password))) {
    		tower.execute();
    	}
    	tower.release();
    	
    	System.out.println("END");
    }
}
