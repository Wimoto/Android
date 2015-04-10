package com.wimoto.app.screens.searchsensor;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.wimoto.app.R;
import com.mobitexoft.leftmenu.PageFragment;
import com.wimoto.app.MainActivity;
import com.wimoto.app.managers.SensorsManager.SensorsManagerListener;
import com.wimoto.app.model.Sensor;

public class SearchSensorFragment extends PageFragment implements SensorsManagerListener {

	private SearchSensorAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHeaderVisibility(View.GONE);
		
		mAdapter = new SearchSensorAdapter();
		((MainActivity)getActivity()).getSensorsManager().addListenerForUnregisteredSensors(this);
	}
	
	@Override
	public void onDestroy() {
		((MainActivity)getActivity()).getSensorsManager().removeListenerForUnregisteredSensors(this);
		super.onDestroy();
	}

	@Override	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.found_sensor_fragment, null);
		
		ListView listView = (ListView)view.findViewById(R.id.found_sensor_listview);
		listView.setAdapter(mAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				registerSensor(mAdapter.getItem(position));
			}
		});
		
		return view;
	}
	
	@Override
	public void didUpdateSensors(ArrayList<Sensor> sensors) {
		mAdapter.updateSensors(sensors);
	}
	
	private void registerSensor(Sensor sensor) {
		MainActivity activity = (MainActivity)getActivity();
		activity.getSensorsManager().registerSensor(sensor);
		activity.showSensorDetails(sensor);
	}
	
}
