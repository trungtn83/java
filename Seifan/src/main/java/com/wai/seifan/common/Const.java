package com.wai.seifan.common;

import java.util.ArrayList;
import java.util.List;

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
    
    long MANA_SMALL_QUALITY = 50;
	
}
