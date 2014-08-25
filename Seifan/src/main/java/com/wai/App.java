package com.wai;

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
    	QuestMysteryRoad mysteryRoad = new QuestMysteryRoad();
    	mysteryRoad.login("hit", password);
    	mysteryRoad.execute();
    }
}
