package com.marknicholas.wimoto;

import android.content.Context;
import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.marknicholas.wimoto.menu.left.LeftMenuFragment;
import com.marknicholas.wimoto.menu.left.MenuItem;
import com.marknicholas.wimoto.menu.right.RightMenuFragment;
import com.marknicholas.wimoto.models.sensor.Sensor;

public class MainActivity extends SlidingFragmentActivity {
	private LeftMenuFragment mLeftMenuFragment;
	private RightMenuFragment mRightMenuFragment;
	
	private static Context sContext;
	
	public static Context getAppContext() {
        return MainActivity.sContext;
    }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        MainActivity.sContext = this;
        
        setMenuMode(SlidingMenu.LEFT_RIGHT);
        
        mLeftMenuFragment = new LeftMenuFragment();
        setLeftMenuFragment(mLeftMenuFragment);
        
        mRightMenuFragment = new RightMenuFragment();
        setRightMenuFragment(mRightMenuFragment);
        
        setBehindOffsetRes(R.dimen.menu_offset);
        setFadeDegree(0.35f);
        setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        
        setShadowWidthRes(R.dimen.menu_shadow_width);
        setShadowDrawable(R.drawable.shadow);
        setSecondaryShadowDrawable(R.drawable.shadow_secondary);
    }

	public void searchSensorAction() {
		mLeftMenuFragment.onMenuItemSelected(MenuItem.SEARCH);
	}

	public void showSensorDetails(Sensor sensor) {
		mRightMenuFragment.showSensorDetails(sensor);
	}
	
	public void updateRegisteredSensors() {
		mRightMenuFragment.updateRegisteredSensors();
	}
}
