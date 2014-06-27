package com.marknicholas.wimoto;

import android.content.Context;
import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.marknicholas.wimoto.database.DBManager;
import com.marknicholas.wimoto.menu.left.LeftMenuFragment;
import com.marknicholas.wimoto.menu.right.RightMenuFragment;

public class MainActivity extends SlidingFragmentActivity {
	
	private static Context sContext;
	
	private LeftMenuFragment mLeftMenuFragment;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        MainActivity.sContext = this;
        
        setMenuMode(SlidingMenu.LEFT_RIGHT);
        
        mLeftMenuFragment = new LeftMenuFragment();
        setLeftMenuFragment(mLeftMenuFragment);
        setRightMenuFragment(new RightMenuFragment());
        
        setBehindOffsetRes(R.dimen.menu_offset);
        setFadeDegree(0.35f);
        setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        
        setShadowWidthRes(R.dimen.menu_shadow_width);
        setShadowDrawable(R.drawable.shadow);
        setSecondaryShadowDrawable(R.drawable.shadow_secondary);
        
        DBManager.initDB();
    }
    
    public static Context getAppContext() {
        return MainActivity.sContext;
    }
    
    public LeftMenuFragment getLeftMenuFragment() {
    	return mLeftMenuFragment;
    }
}
