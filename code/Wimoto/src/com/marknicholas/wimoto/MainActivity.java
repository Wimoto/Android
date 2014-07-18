package com.marknicholas.wimoto;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.marknicholas.wimoto.managers.SensorsManager;
import com.marknicholas.wimoto.menu.left.LeftMenuFragment;
import com.marknicholas.wimoto.menu.left.MenuItem;
import com.marknicholas.wimoto.menu.right.RightMenuFragment;
import com.marknicholas.wimoto.models.sensor.Sensor;
import com.marknicholas.wimoto.utils.AppContext;

public class MainActivity extends SlidingFragmentActivity {
	
	private static int REQUEST_ENABLE_BT 	= 19780;
	
	private LeftMenuFragment mLeftMenuFragment;
	private RightMenuFragment mRightMenuFragment;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AppContext.setContext(this);
        
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
        	Toast.makeText(this, R.string.alert_ble_not_supported, Toast.LENGTH_LONG).show();
        	
        	finish();
        }
        
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

	@Override
	protected void onStart() {
		super.onStart();
				
		if (SensorsManager.isBluetoothEnabled()) {
			SensorsManager.startScan();
		} else {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		SensorsManager.stopScan();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		Log.e("", "Main Activity onDestroy");
		SensorsManager.stopScan();
		SensorsManager.disconnectGatts();
	}

	public void searchSensorAction() {
		mLeftMenuFragment.onMenuItemSelected(MenuItem.SEARCH);
	}

	public void showSensorDetails(Sensor sensor) {
		mRightMenuFragment.showSensorDetails(sensor);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if ((requestCode == REQUEST_ENABLE_BT) && (resultCode == Activity.RESULT_OK)) {
			SensorsManager.startScan();
		}		
	}
}