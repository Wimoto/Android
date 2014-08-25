package com.wimoto.app.menu.left;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wimoto.app.R;

public class LeftMenuView extends RelativeLayout {
	
	private MenuItem mMenuItem;
	
	private TextView mTitleView;

	public LeftMenuView(Context context) {
		super(context);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.menu_left_cell, this);
        
        mTitleView = (TextView)findViewById(R.id.left_menu_item);
	}
	
	public void setMenuItem(MenuItem menuItem) {
		this.mMenuItem = menuItem;
		
		mTitleView.setText(mMenuItem.getTitle());
	}
	
}
