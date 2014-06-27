package com.mobitexoft.navigation;

import java.util.Stack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

public class NavigationFragment extends Fragment {
	
	private Stack<SlideFragment> mFragmentStack;
	
	public static NavigationFragment newInstance(PageFragment rootFragment) {
		Stack<SlideFragment> stack = new Stack<SlideFragment>();
		stack.push(SlideFragment.newInstance(rootFragment, false));
		
		NavigationFragment navigationFragment = new NavigationFragment();
		navigationFragment.mFragmentStack = stack;
		return navigationFragment;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getChildFragmentManager().beginTransaction().add(R.id.contentLayout, mFragmentStack.lastElement()).commit();
		
        return inflater.inflate(R.layout.fragment_navigation, container, false);
    }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (mFragmentStack != null) {
			SlideFragment currentSlideFragment = (SlideFragment)mFragmentStack.lastElement();
			if (currentSlideFragment != null) {
				currentSlideFragment.onActivityResult(requestCode, resultCode, data);
			}
		}
	}
	
	public void pushFragment(PageFragment fragment) {
		SlideFragment pushedFragment = SlideFragment.newInstance(fragment, true);
		
		FragmentManager fragmentManager = getChildFragmentManager();
		
		FragmentTransaction addTransaction = fragmentManager.beginTransaction();
		addTransaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out, R.anim.slide_right_in, R.anim.slide_right_out);
		addTransaction.add(R.id.contentLayout, pushedFragment);
		addTransaction.commit();
		
		FragmentTransaction visibilityTransaction = fragmentManager.beginTransaction();
		visibilityTransaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out, R.anim.slide_right_in, R.anim.slide_right_out);
		visibilityTransaction.show(pushedFragment);
		visibilityTransaction.hide(mFragmentStack.lastElement());
		visibilityTransaction.addToBackStack(null);
		visibilityTransaction.commit();
		
		mFragmentStack.push(pushedFragment);
	}
			
	public boolean popFragment() {
		hideKeyboard();
		
		mFragmentStack.pop();
		if (getChildFragmentManager().getBackStackEntryCount()>0) {
			getChildFragmentManager().popBackStack();
			return true;
		}
		return false;
	}
	
	public void hideKeyboard() {
    	if (getActivity().getCurrentFocus() != null) {
    		InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    		inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    	}
    }
	
}

class SlideFragment extends Fragment {
	
	private PageFragment mContentFragment;
	private boolean pushed;
	
	public static SlideFragment newInstance(PageFragment contentFragment, boolean pushed) {
		SlideFragment slideFragment = new SlideFragment();
		slideFragment.mContentFragment = contentFragment;
		slideFragment.pushed = pushed;
		return slideFragment;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_slide, container, false);
		
		FragmentManager manager = getChildFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		ft.add(R.id.contentLayout, mContentFragment);
		ft.commit();
			
		if (pushed) {
			view.findViewById(R.id.backControlLayout).setVisibility(View.VISIBLE);
			view.findViewById(R.id.leftControlLayout).setVisibility(View.INVISIBLE);
		} else {
			view.findViewById(R.id.backControlLayout).setVisibility(View.INVISIBLE);
			view.findViewById(R.id.leftControlLayout).setVisibility(View.VISIBLE);
		}
		
        return view;
    }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (mContentFragment != null) {
			mContentFragment.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	public void setHeaderVisibility(int visibility) {
		getView().findViewById(R.id.headerLayout).setVisibility(visibility);
	}
	
	public void setBackControlView(View view) {
		FrameLayout backControlLayout = (FrameLayout) getView().findViewById(R.id.backControlLayout);
		backControlLayout.removeAllViews();
		backControlLayout.addView(view);
	}
	
	public void setLeftControlView(View view) {
		FrameLayout leftControlLayout = (FrameLayout) getView().findViewById(R.id.leftControlLayout);
		leftControlLayout.removeAllViews();
		leftControlLayout.addView(view);
	}
	
	public void setTitleView(View view) {
		FrameLayout titleLayout = (FrameLayout) getView().findViewById(R.id.titleLayout);
		titleLayout.removeAllViews();
		titleLayout.addView(view);
	}
	
	public void setRightControlView(View view) {
		FrameLayout rightControlLayout = (FrameLayout) getView().findViewById(R.id.rightControlLayout);
		rightControlLayout.removeAllViews();
		rightControlLayout.addView(view);
	}
}
