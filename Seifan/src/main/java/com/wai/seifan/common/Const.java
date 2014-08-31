package com.wai.seifan.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Const {
	double[] RATIO_POTENTIAL_POINT = {0.6, 0.4, 0};
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
    
    long MANA_SMALL_QUALITY = 50;
	
}
