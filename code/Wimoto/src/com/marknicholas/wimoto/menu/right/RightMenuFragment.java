package com.marknicholas.wimoto.menu.right;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.marknicholas.wimoto.R;
import com.marknicholas.wimoto.models.sensor.ClimateSensor;
import com.marknicholas.wimoto.models.sensor.GrowSensor;
import com.marknicholas.wimoto.models.sensor.Sensor;
import com.marknicholas.wimoto.models.sensor.SentrySensor;
import com.marknicholas.wimoto.models.sensor.ThermoSensor;
import com.marknicholas.wimoto.models.sensor.WaterSensor;
import com.marknicholas.wimoto.screens.sensor.SensorFragment;
import com.marknicholas.wimoto.screens.sensor.climate.ClimateSensorFragment;
import com.marknicholas.wimoto.screens.sensor.grow.GrowSensorFragment;
import com.marknicholas.wimoto.screens.sensor.sentry.SentrySensorFragment;
import com.marknicholas.wimoto.screens.sensor.thermo.ThermoSensorFragment;
import com.marknicholas.wimoto.screens.sensor.water.WaterSensorFragment;
import com.mobitexoft.navigation.NavigationFragment;

public class RightMenuFragment extends Fragment {
	
	private static final String TAG_SENSOR = "sensor_info";
	
	private RightMenuAdapter mAdapter;
	
	private SlidingFragmentActivity mSlidingFragmentActivity;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mSlidingFragmentActivity = (SlidingFragmentActivity)getActivity();
	}
	
	@Override	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menu_right_fragment, null);
		
		mAdapter = new RightMenuAdapter();
		
		ListView listView = (ListView)view.findViewById(R.id.right_menu_listview);
		listView.setAdapter(mAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				showSensorDetails(mAdapter.getItem(position));
			}
		});
	
		return view;
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