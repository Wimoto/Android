package com.marknicholas.wimoto.screens.sensor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marknicholas.wimoto.MainActivity;
import com.marknicholas.wimoto.R;
import com.mobitexoft.leftmenu.PageFragment;

public class NoSensorFragment extends PageFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHeaderVisibility(View.GONE);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.nosensor_fragment, null);
				
		TextView addNewSensor = (TextView)view.findViewById(R.id.sensor_add);
		addNewSensor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				foundSensorsAction();
			}
			
		});
		
		return view;
	}
	private void foundSensorsAction() {
		MainActivity activity = (MainActivity)getActivity();
		activity.getLeftMenuFragment().foundSensorAction();
	}
}
