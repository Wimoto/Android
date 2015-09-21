package com.wimoto.app;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.wimoto.app.managers.SensorsManager;

public class AppContext extends SlidingFragmentActivity {
	
	protected SensorsManager mSensorsManager;
	
	public void showAlert(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(message)
    			.setCancelable(false)
    			.setNegativeButton("OK",
    					new DialogInterface.OnClickListener() {
    						public void onClick(DialogInterface dialog, int id) {
    							dialog.cancel();
    						}
    					});
    	AlertDialog alert = builder.create();
    	alert.show();
	}

	public SensorsManager getSensorsManager() {
		return mSensorsManager;
	}
}
