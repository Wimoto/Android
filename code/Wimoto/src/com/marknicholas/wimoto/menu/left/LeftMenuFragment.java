package com.marknicholas.wimoto.menu.left;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.marknicholas.wimoto.R;
import com.marknicholas.wimoto.models.sensor.ClimateSensor;
import com.marknicholas.wimoto.models.sensor.GrowSensor;
import com.marknicholas.wimoto.models.sensor.Sensor;
import com.marknicholas.wimoto.models.sensor.SentrySensor;
import com.marknicholas.wimoto.models.sensor.ThermoSensor;
import com.marknicholas.wimoto.models.sensor.WaterSensor;
import com.marknicholas.wimoto.screens.foundsensor.FoundSensorFragment;
import com.marknicholas.wimoto.screens.help.HelpFragment;
import com.marknicholas.wimoto.screens.sensor.NoSensorFragment;
import com.marknicholas.wimoto.screens.sensor.SensorFragment;
import com.marknicholas.wimoto.screens.sensor.climate.ClimateSensorFragment;
import com.marknicholas.wimoto.screens.sensor.grow.GrowSensorFragment;
import com.marknicholas.wimoto.screens.sensor.sentry.SentrySensorFragment;
import com.marknicholas.wimoto.screens.sensor.thermo.ThermoSensorFragment;
import com.marknicholas.wimoto.screens.sensor.water.WaterSensorFragment;
import com.marknicholas.wimoto.screens.settings.SettingsFragment;
import com.mobitexoft.navigation.NavigationFragment;

public class LeftMenuFragment extends ListFragment {

	private static final int MENU_ITEM_ADD_NEW_SENSOR = 0;
	private static final int MENU_ITEM_ADD_FOUND_SENSOR = 0;
	private static final int MENU_ITEM_SETTINGS = 1;
	private static final int MENU_ITEM_HELP = 2;
	
	private static final String TAG_ADD_NEW_SENSOR = "sensor";
	private static final String TAG_SENSOR = "sensor_info";
	private static final String TAG_SETTINGS = "settings";
	private static final String TAG_HELP = "help";
	private static final String TAG_FOUND = "found";
	
	private SlidingFragmentActivity mSlidingFragmentActivity;
	private boolean isFoundSensors;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menu_left_fragment, null);
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mSlidingFragmentActivity = (SlidingFragmentActivity)getActivity();
		
		String[] menuItems = getResources().getStringArray(R.array.left_menu_items);
		ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getActivity(), R.layout.menu_left_cell, R.id.left_menu_item, menuItems);
		setListAdapter(menuAdapter);
		
		selectMenuItem(MENU_ITEM_ADD_NEW_SENSOR);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		selectMenuItem(position);		
	}

	private void selectMenuItem(int position) {
		if (position == MENU_ITEM_ADD_NEW_SENSOR) {
			if (!isFoundSensors) {
				mSlidingFragmentActivity.pushFragmentToCenterSlide(NavigationFragment.newInstance(new NoSensorFragment()), TAG_ADD_NEW_SENSOR);
			} else {
				mSlidingFragmentActivity.pushFragmentToCenterSlide(NavigationFragment.newInstance(new FoundSensorFragment()), TAG_FOUND);
			}
		} else if (position == MENU_ITEM_SETTINGS) {
			mSlidingFragmentActivity.pushFragmentToCenterSlide(NavigationFragment.newInstance(new SettingsFragment()), TAG_SETTINGS);
		} else if (position == MENU_ITEM_HELP) {
			mSlidingFragmentActivity.pushFragmentToCenterSlide(NavigationFragment.newInstance(new HelpFragment()), TAG_HELP);
		}
	}
	
	public void foundSensorAction() {
		isFoundSensors = true;
		selectMenuItem (MENU_ITEM_ADD_FOUND_SENSOR);
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
		
		mSlidingFragmentActivity.pushFragmentToCenterSlide(NavigationFragment.newInstance(fragment), TAG_SENSOR);
	}

}
