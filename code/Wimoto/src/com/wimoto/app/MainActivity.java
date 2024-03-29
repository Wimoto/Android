package com.wimoto.app;

import java.util.ArrayList;
import java.util.Locale;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.mobitexoft.leftmenu.PageFragment;
import com.mobitexoft.navigation.NavigationFragment;
import com.wimoto.app.managers.SensorsManager;
import com.wimoto.app.managers.SensorsManager.SensorsManagerListener;
import com.wimoto.app.menu.left.LeftMenuFragment;
import com.wimoto.app.menu.left.MenuItem;
import com.wimoto.app.menu.right.RightMenuFragment;
import com.wimoto.app.model.sensors.Sensor;
import com.wimoto.app.screens.sensor.NoSensorFragment;
import com.wimoto.app.screens.sensor.SensorFragment;

public class MainActivity extends AppContext implements SensorsManagerListener {
	
	private static final String TAG_NO_SENSOR = "no_sensor_tag";
	
	private static int REQUEST_ENABLE_BT 	= 19780;
		
	private LeftMenuFragment mLeftMenuFragment;
	private RightMenuFragment mRightMenuFragment;
	
	private Fragment mCurrentFragment;
	
	private Sensor mCurrentSensor;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		Configuration configuration = new Configuration(Resources.getSystem().getConfiguration());
		configuration.locale = Locale.US; 
		Resources.getSystem().updateConfiguration(configuration, null);
        
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
        	Toast.makeText(this, R.string.alert_ble_not_supported, Toast.LENGTH_LONG).show();
        	
        	finish();
        }
        
        mSensorsManager = new SensorsManager(this);
        
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
							
		mSensorsManager.addListenerForRegisteredSensors(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		//stopScan();
	}
	
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//
//		if ((requestCode == REQUEST_ENABLE_BT) && (resultCode == Activity.RESULT_OK)) {
//			mSensorsManager.startScan();
//		}		
//	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		Log.e("", "Main Activity onDestroy");
		
		mSensorsManager.removeListenerForRegisteredSensors(this);
		//stopScan();
		mSensorsManager.disconnectGatts();
	}

	@Override
	public void pushFragmentToCenterSlide(Fragment fragment, String tag) {
		if ((fragment != null) && (!fragment.equals(mCurrentFragment))) {
			mCurrentFragment = fragment;
			
			super.pushFragmentToCenterSlide(NavigationFragment.newInstance((PageFragment)fragment), tag);
		}
	}

//	public void startScan() {
//		if (mSensorsManager.isBluetoothEnabled()) {
//			mSensorsManager.startScan();
//		} else {
//			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//		}
//	}
	
//	public void stopScan() {
//		mSensorsManager.stopScan();
//	}
	
	public void searchSensorAction() {
		mLeftMenuFragment.onMenuItemSelected(MenuItem.SEARCH);
	}

	public void showSensorDetails(Sensor sensor) {
		if (sensor == null) {
			if ((mCurrentFragment == null) || (mCurrentFragment instanceof SensorFragment)) {
				pushFragmentToCenterSlide(new NoSensorFragment(), TAG_NO_SENSOR);
			}
		} else {
			if (sensor.equals(mCurrentSensor) && (mCurrentFragment instanceof SensorFragment)) {
				toggle();
			} else {
				mCurrentSensor = sensor;
				
				SensorFragment sensorfragment = SensorFragment.createSensorFragment(sensor);
				pushFragmentToCenterSlide(sensorfragment, sensorfragment.getFragmentId());
			}
		}
	}
		
	@Override
	public void didUpdateSensors(ArrayList<Sensor> sensors) {
		Sensor sensor = null;
		if (sensors.size() > 0) {
			sensor = sensors.get(0);
		}
		
		showSensorDetails(sensor);
	}
}