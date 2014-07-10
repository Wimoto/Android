package com.marknicholas.wimoto.menu.right;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.marknicholas.wimoto.R;
import com.marknicholas.wimoto.managers.SensorsManager;
import com.marknicholas.wimoto.models.sensor.ClimateSensor;
import com.marknicholas.wimoto.models.sensor.GrowSensor;
import com.marknicholas.wimoto.models.sensor.Sensor;
import com.marknicholas.wimoto.models.sensor.SentrySensor;
import com.marknicholas.wimoto.models.sensor.ThermoSensor;
import com.marknicholas.wimoto.models.sensor.WaterSensor;
import com.marknicholas.wimoto.screens.sensor.NoSensorFragment;
import com.marknicholas.wimoto.screens.sensor.SensorFragment;
import com.marknicholas.wimoto.screens.sensor.climate.ClimateSensorFragment;
import com.marknicholas.wimoto.screens.sensor.grow.GrowSensorFragment;
import com.marknicholas.wimoto.screens.sensor.sentry.SentrySensorFragment;
import com.marknicholas.wimoto.screens.sensor.thermo.ThermoSensorFragment;
import com.marknicholas.wimoto.screens.sensor.water.WaterSensorFragment;
import com.marknicholas.wimoto.utils.SwipeDetector;
import com.mobitexoft.navigation.NavigationFragment;

public class RightMenuFragment extends Fragment {
	
	private static final String TAG_NO_SENSOR = "no_sensor_tag";
	private static final String TAG_SENSOR = "sensor_tag";
	
	private RightMenuAdapter mAdapter;
	private SwipeDetector mSwipeDetector;
	
	private SlidingFragmentActivity mSlidingFragmentActivity;
	
	@Override	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menu_right_fragment, null);
		
		mAdapter = new RightMenuAdapter();
		
		ListView listView = (ListView)view.findViewById(R.id.right_menu_listview);
		listView.setAdapter(mAdapter);
		
		mSwipeDetector = new SwipeDetector();
		
		listView.setOnTouchListener(mSwipeDetector);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(mSwipeDetector.swipeDetected()) {
					
					RightMenuView rightMenuView = (RightMenuView)view;
					
		            if(mSwipeDetector.getAction() == SwipeDetector.Action.RL) {
		            	rightMenuView.setDeleteMode();
		            } else if(mSwipeDetector.getAction() == SwipeDetector.Action.LR) {
		            	rightMenuView.setNormalMode();
		            } 
		        } else {
	            	showSensorDetails(mAdapter.getItem(position));
	            }
			}
		});
		
		showDefaultSensor();
		
		return view;
	}
	
	private void showDefaultSensor() {
		mSlidingFragmentActivity = (SlidingFragmentActivity)getActivity();
		
		if (SensorsManager.getManager().getSensors().size() > 0) {
			showSensorDetails(SensorsManager.getManager().getSensors().get(0));
		} else {
			mSlidingFragmentActivity.pushFragmentToCenterSlide(NavigationFragment.newInstance(new NoSensorFragment()), TAG_NO_SENSOR);
		}
	}
	
	public void showSensorDetails(Sensor sensor) {
		SensorFragment fragment = null;
		
		if (sensor instanceof ClimateSensor) {
			fragment = new ClimateSensorFragment();
		} else if (sensor instanceof GrowSensor) {
			fragment = new GrowSensorFragment();
		} else if (sensor instanceof SentrySensor) {
			fragment = new SentrySensorFragment();
		} else if (sensor instanceof ThermoSensor) {
			fragment = new ThermoSensorFragment();
		} else if (sensor instanceof WaterSensor) {
			fragment = new WaterSensorFragment();
		}
		
		fragment.setSensor(sensor);

		Log.e("", "sensor id " + sensor.getId());
		
		mSlidingFragmentActivity.pushFragmentToCenterSlide(NavigationFragment.newInstance(fragment), TAG_SENSOR + sensor.getId());
		SensorsManager.getManager().registerSensor(sensor);
	}
	
	public void updateRegisteredSensors() {
		mAdapter.updateRegisteredSensors();
	}
}