package com.wimoto.app.menu.left;

import java.util.ArrayList;

import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wimoto.app.AppContext;
import com.wimoto.app.R;

public class LeftMenuAdapter extends BaseAdapter {

	private AppContext mContext;
	
	private ArrayList<MenuItem> mMenuItems;
	
	private Resources mResources;
	
	public LeftMenuAdapter(AppContext context) {
		mContext = context;
		
		mResources = mContext.getResources();
		
		initMenuItems();
	}
	
	private void initMenuItems() {
		mMenuItems = new ArrayList<MenuItem> ();
		
		mMenuItems.add(new MenuItem(mResources.getString(R.string.left_menu_add_sensor), MenuItem.SEARCH));
		mMenuItems.add(new MenuItem(mResources.getString(R.string.left_menu_settings), MenuItem.SETTINGS));
		mMenuItems.add(new MenuItem(mResources.getString(R.string.left_menu_help), MenuItem.HELP));
	}
	
	@Override
	public int getCount() {
		return mMenuItems.size();
	}

	@Override
	public MenuItem getItem(int position) {
		return mMenuItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MenuItem item = getItem(position);
		if (convertView == null) {
			convertView = new LeftMenuView(mContext);
		}
		((LeftMenuView)convertView).setMenuItem(item);
		
		return convertView;
	}

}
