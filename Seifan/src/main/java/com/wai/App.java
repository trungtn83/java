package com.wai;

import com.wai.seifan.quest.AcceptFriend;
import com.wai.seifan.quest.QuestDragonTower;
import com.wai.seifan.quest.QuestMysteryRoad;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
    	String username = args[0];
    	String password = "hatrung";
    	
//    	Adventure adv = new Adventure();
//    	adv.login(username, password);
//    	adv.execute();
    	
//    	QuestMysteryRoad mysteryRoad = new QuestMysteryRoad(false, false, false, false);
//    	mysteryRoad.login(username, password);
//    	mysteryRoad.execute();
    	
//    	QuestDragonTower dragonTower = new QuestDragonTower(false, false, false, false);
//    	dragonTower.login(username, password);
//    	dragonTower.execute();
    	
    	AcceptFriend add = new AcceptFriend();
    	add.login(username, password);
    	add.execute();
    }
}
