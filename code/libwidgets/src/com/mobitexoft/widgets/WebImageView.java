package com.mobitexoft.widgets;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView;

public class WebImageView extends ImageView {

	private final String NAMESPACE = "http://schemas.android.com/apk/res/android";
	private final String SRC_KEY = "src"; 
	private final int DEFAULT_PLACEHOLDER = android.R.color.transparent;
	
	private String URLString;
	private DownloadWebImageTask downloadTask;
	
	private int placeholderResource;
	
	private Context mContext;
	
	public WebImageView(Context context) {
		super(context);	
		
		mContext = context;
	}

	public WebImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		mContext = context;
		
		placeholderResource = attrs.getAttributeResourceValue(NAMESPACE, SRC_KEY, DEFAULT_PLACEHOLDER);
	}

	public WebImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mContext = context;
		
		placeholderResource = attrs.getAttributeResourceValue(NAMESPACE, SRC_KEY, DEFAULT_PLACEHOLDER);
	}
	
	public void setImageURI(Uri uri) {
		super.setImageURI(uri);
		
		if (downloadTask != null) {
			downloadTask.cancel(true);
		}
	}
	
	public void setURLString(String URLString) {
		if ((URLString != null) && (URLString.equals(this.URLString))) {
			return;
		}
		this.URLString = URLString;
			
		setImageResource(placeholderResource);
		
		if (downloadTask != null) {
			downloadTask.cancel(true);
		}
		
		if (URLString != null) {
			downloadTask = new DownloadWebImageTask();
			downloadTask.execute(URLString);
		}
	}
	
	private class DownloadWebImageTask extends AsyncTask<String, Void, Bitmap> {
		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bitmap = null;
			
			InputStream inputStream = null;
			try {
				String imageUrl = params[0];
				String filePath = generatePathFromUrl(imageUrl);
				
				File file = new File(filePath);
				if (file.exists()) {					
					bitmap = BitmapFactory.decodeFile(filePath);					
				}
				
				if (bitmap == null) {
					URL url = new URL(imageUrl);
					HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
					inputStream = new BufferedInputStream(
							urlConnection.getInputStream());

					bitmap = BitmapFactory.decodeStream(inputStream);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 80,
							new FileOutputStream(filePath));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block				
			} finally {
				try {
					inputStream.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
				}
			}
			return bitmap;
		}
		
        @Override
        protected void onPostExecute(Bitmap downloadedBitmap) {
        	if (downloadedBitmap == null) {
        		setImageResource(placeholderResource);
        	} else {
                if (isCancelled()) {
                	downloadedBitmap = null;
                }
        		
        		setImageBitmap(downloadedBitmap);
        	}
       }
	}
	
	private String generatePathFromUrl(String url){
	    String uniqueName = url.replace("://", "_").replace(".", "_").replace("/", "_");
	    uniqueName = uniqueName.substring(0,uniqueName.lastIndexOf('_'))
	            +"."+uniqueName.substring(uniqueName.lastIndexOf('_')+1,uniqueName.length());
	    
	    return mContext.getCacheDir().getPath().toString() + uniqueName;
	}
}
