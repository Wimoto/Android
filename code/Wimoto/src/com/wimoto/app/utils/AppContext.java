package com.wimoto.app.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AppContext {
	
	private static Context sContext;

	public static Context getContext() {
		return sContext;
	}

	public static void setContext(Context context) {
		AppContext.sContext = context;		
	}
	
	public static void showAlert(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(AppContext.getContext());
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
	
	public static String getString(int resId) {
		return sContext.getResources().getString(resId);
	}	
}
