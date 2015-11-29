package com.wimoto.app.screens.searchsensor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.mobitexoft.leftmenu.PageFragment;
import com.wimoto.app.AppContext;
import com.wimoto.app.MainActivity;
import com.wimoto.app.R;
import com.wimoto.app.bluetooth.WimotoDevice;

public class SearchSensorFragment extends PageFragment {

	private SearchSensorAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHeaderVisibility(View.GONE);
		
		mAdapter = new SearchSensorAdapter((AppContext) getActivity());
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
				registerDevice(mAdapter.getItem(position));
			}
		});
		
		return view;
	}

	private void registerDevice(WimotoDevice device) {
		MainActivity mainActivity = (MainActivity) getActivity();
		mainActivity.getSensorsManager().registerDevice(device);	
	}
}
