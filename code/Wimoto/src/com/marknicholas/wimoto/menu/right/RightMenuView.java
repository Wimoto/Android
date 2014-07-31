package com.marknicholas.wimoto.menu.right;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marknicholas.wimoto.R;
import com.marknicholas.wimoto.model.Sensor;

public class RightMenuView extends RelativeLayout {
	
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
		if (mSensor == sensor) {
			return;
		}
		
		this.mSensor = sensor;
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

}
