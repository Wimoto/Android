package com.marknicholas.wimoto.menu.right;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.wimoto.app.R;
import com.marknicholas.wimoto.managers.SensorsManager.SensorsManagerListener;
import com.marknicholas.wimoto.model.Sensor;
import com.marknicholas.wimoto.utils.SwipeDetector;
import com.wimoto.app.MainActivity;

public class RightMenuFragment extends Fragment implements SensorsManagerListener {
	
	private RightMenuAdapter mAdapter;
	private SwipeDetector mSwipeDetector;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mAdapter = new RightMenuAdapter(getActivity());
		((MainActivity)getActivity()).getSensorsManager().addListenerForRegisteredSensors(this);
	}
	
	@Override
	public void onDestroy() {
		((MainActivity)getActivity()).getSensorsManager().removeListenerForRegisteredSensors(this);
		super.onDestroy();
	}
	
	@Override	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menu_right_fragment, null);
		
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
		        	((MainActivity)getActivity()).showSensorDetails(mAdapter.getItem(position));
	            }
			}
		});
		
		return view;
	}
	
	@Override
	public void didUpdateSensors(ArrayList<Sensor> sensors) {
		mAdapter.updateSensors(sensors);		
	}
}