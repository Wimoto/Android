package com.wimoto.app.widgets;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.wimoto.app.MainActivity;
import com.wimoto.app.model.sensors.Sensor;

public class LastUpdateTextView extends TextView {

	private Context mContext;
	
	private Sensor mSensor;
	private Timer mLastUpdateTimer;
	
	public LastUpdateTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.mContext = context;
	}
	
	public void setSensor(Sensor sensor) {
		this.mSensor = sensor;
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		
		reset();
	}

	public void reset() {
		if (mLastUpdateTimer != null) {
			mLastUpdateTimer.cancel();	
		}
		this.mLastUpdateTimer = null;
	}
	
	public void refresh() {
		setText("Just now");
		
		if (mLastUpdateTimer != null) {
			mLastUpdateTimer.cancel();
		}
		
		TimerTask task = new TimerTask()
        {
           @Override
           public void run()
           {
        	   ((MainActivity)mContext).runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                	   refreshLastUpdate();
                   }
               });
           }
        };
        mLastUpdateTimer = new Timer();
        mLastUpdateTimer.schedule(task, 15000, 15000);
	}
	
	private void refreshLastUpdate() {
		CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(mSensor.getLastActivityDate().getTime(), System.currentTimeMillis(), 0L, DateUtils.FORMAT_ABBREV_ALL);
		setText(relativeTime);
	}

}
