package com.wimoto.app.menu.right;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wimoto.app.R;
import com.wimoto.app.model.Sensor;

public class RightMenuView extends RelativeLayout implements Observer {
	
	private Sensor mSensor;
	
	private TextView mTitleView;
	private ImageView mLogoView;
	private View mDeleteView;

	public RightMenuView(Context context) {
		super(context);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.menu_right_cell, this);
        
        mTitleView = (TextView)findViewById(R.id.sensor_title);
        mLogoView = (ImageView)findViewById(R.id.wimoto_logo);
        mDeleteView = findViewById(R.id.delete_button);
	}
	
	public void setSensor(Sensor sensor) {
		if ((mSensor != null) && (mSensor.equals(sensor))) {
			return;
		}
		
		if (mSensor != null) {
			mSensor.deleteObserver(this);
		}
		
		this.mSensor = sensor;
		mSensor.addObserver(this);
		
		setNormalMode();
		
		mTitleView.setText(mSensor.getTitle());
	}
	
	public void setNormalMode() {
		mLogoView.setVisibility(View.VISIBLE);
		mDeleteView.setVisibility(View.GONE);
	}
	
	public void setDeleteMode() {
		mLogoView.setVisibility(View.GONE);
		mDeleteView.setVisibility(View.VISIBLE);
	}

	@Override
	public void update(Observable observable, Object data) {
		mTitleView.setText(mSensor.getTitle());		
	}

}
