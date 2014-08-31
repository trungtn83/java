package com.wai;

<<<<<<< HEAD
import com.wai.seifan.quest.AcceptFriend;
import com.wai.seifan.quest.QuestDragonTower;
=======
>>>>>>> 4db9abfdaed85bee8c8e767fce22b70e00cefc41
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
