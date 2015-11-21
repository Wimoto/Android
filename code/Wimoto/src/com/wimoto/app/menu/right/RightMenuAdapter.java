package com.wimoto.app.menu.right;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wimoto.app.R;
import com.wimoto.app.MainActivity;
import com.wimoto.app.model.sensors.Sensor;

public class RightMenuAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Sensor> mSensors;

	public RightMenuAdapter(Context context) {
		mContext = context;
		mSensors = new ArrayList<Sensor>();
	}
	
	public void updateSensors(ArrayList<Sensor> sensors) {
		mSensors.clear();
		mSensors.addAll(sensors);
		
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mSensors.size();
	}

	@Override
	public Sensor getItem(int position) {
		return mSensors.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		RightMenuView rightMenuView = null;
		if (convertView == null) {
			rightMenuView = new RightMenuView(mContext);
		} else {
			rightMenuView = (RightMenuView)convertView;
		}
		
		rightMenuView.setSensor(mSensors.get(position));
		
		View deleteButton = rightMenuView.findViewById(R.id.delete_button);
		deleteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteSensor(mSensors.get(position));
			}
		});
		
		return rightMenuView;
	}

	private void deleteSensor(Sensor sensor) {
		((MainActivity)mContext).getSensorsManager().unregisterSensor(sensor);
	}
}
