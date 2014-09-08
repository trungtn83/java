package com.wai;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args ) throws Exception
    {
//    	String username = args[0];
//    	String password = "hatrung";
    	
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
    		
    	try {

            System.setProperty("java.net.useSystemProxies","true");
            List l = ProxySelector.getDefault().select(
                        new URI("http://www.yahoo.com/"));

            for (Iterator iter = l.iterator(); iter.hasNext(); ) {

                Proxy proxy = (Proxy) iter.next();

                System.out.println("proxy hostname : " + proxy.type());

                InetSocketAddress addr = (InetSocketAddress)
                    proxy.address();

                if(addr == null) {

                    System.out.println("No Proxy");

                } else {

                    System.out.println("proxy hostname : " + 
                            addr.getHostName());

                    System.out.println("proxy port : " + 
                            addr.getPort());

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
//    	AcceptFriend add = new AcceptFriend();
//    	add.login(username, password);
//    	add.execute();
    	System.out.println("END ");
    }
}
