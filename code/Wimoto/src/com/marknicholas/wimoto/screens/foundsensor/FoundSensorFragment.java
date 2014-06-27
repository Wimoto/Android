package com.marknicholas.wimoto.screens.foundsensor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.marknicholas.wimoto.MainActivity;
import com.marknicholas.wimoto.R;
import com.marknicholas.wimoto.models.sensor.Sensor;
import com.mobitexoft.leftmenu.PageFragment;

public class FoundSensorFragment extends PageFragment {

	private FoundSensorAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHeaderVisibility(View.GONE);
		super.onCreate(savedInstanceState);
	}
	
	@Override	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.found_sensor_fragment, null);
	
		mAdapter = new FoundSensorAdapter();
		
		ListView listView = (ListView)view.findViewById(R.id.found_sensor_listview);
		listView.setAdapter(mAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				showSensorDetails(mAdapter.getItem(position));
			}
		});
		
		return view;
	}

	protected void showSensorDetails(Sensor sensor) {
		MainActivity activity = (MainActivity)getActivity();
		activity.getLeftMenuFragment().showSensorDetails(sensor);
	}
	
}
