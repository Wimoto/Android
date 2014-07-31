package com.marknicholas.wimoto.screens.searchsensor;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.marknicholas.wimoto.MainActivity;
import com.marknicholas.wimoto.R;
import com.marknicholas.wimoto.managers.SensorsManager;
import com.marknicholas.wimoto.managers.SensorsManager.SensorsManagerListener;
import com.marknicholas.wimoto.model.Sensor;
import com.mobitexoft.leftmenu.PageFragment;

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
		((MainActivity)getActivity()).getSensorsManager().registerSensor(sensor);
	}
	
}
