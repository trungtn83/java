package com.wai.seifan.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wai.seifan.dto.UserInfo;

public interface Const {
	double[] RATIO_POTENTIAL_POINT = {1, 0, 0};
	double[] RATIO_ADVENTURE = { 
			0,	// level 0 
			2.33 
	};
	
	String ITEM_ID_MANA_FULL_OPENED = "1";
	String ITEM_ID_MANA_FULL_LOCKED = "8";
	String ITEM_ID_MANA_SMALL = "100";
	
	List<Character> CHAR_INVALID = new ArrayList<Character>(){
        {
            add((char) 32);
            add((char) 160);
        }
    };
    
    List<String> ACCOUNT_MAIN = new ArrayList<String>(){
        {
            add("hit");
            add("marry");
        }
    };
    
    Map<String, String> ACCOUNT_NAME_ID = new HashMap<String, String>(){
        {
            put("You're my everything!", "121");
            put("hantt0409", "85");
            put("you make me cry", "41");
            put("marry", "78");
            put("hit", "107");
            put("wai", "48");
            put("thanhha0409", "83");
        }
    };
    
    UserInfo[] FRIENDS = {
		new UserInfo("148", "loser001", "hatrung")
		,new UserInfo("150", "loser002", "hatrung")
		,new UserInfo("151", "loser003", "hatrung")
		,new UserInfo("152", "loser004", "hatrung")
		,new UserInfo("153", "loser005", "hatrung")
		,new UserInfo("154", "loser006", "hatrung")
		,new UserInfo("155", "loser007", "hatrung")
		,new UserInfo("156", "loser008", "hatrung")
		,new UserInfo("157", "loser009", "hatrung")
		,new UserInfo("158", "loser000", "hatrung")
		,new UserInfo("121", "You're my everything!", "hatrung")
		,new UserInfo("85", "hantt0409", "hatrung")
		,new UserInfo("41", "you make me cry", "hatrung")
		,new UserInfo("78", "marry", "hatrung")
		,new UserInfo("107", "hit", "hatrung")
		,new UserInfo("48", "wai", "hatrung")
		,new UserInfo("83", "thanhha0409", "hatrung")
		,new UserInfo("137", "moingaymotniemvui", "654321")
		,new UserInfo("103", "lina", "magician")
		,new UserInfo("103", "25 Minutes", "654321")
	};
    
    long MANA_SMALL_QUALITY = 50;
    
    long WAITTIME = 2000;
	
}
