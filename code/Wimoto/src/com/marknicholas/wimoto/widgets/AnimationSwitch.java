package com.marknicholas.wimoto.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SoundEffectConstants;
import android.widget.Checkable;
import android.widget.ImageView;

import com.marknicholas.wimoto.R;

public class AnimationSwitch extends ImageView implements Checkable {
	
	public static interface OnCheckedChangeListener {
    	void  onCheckedChanged(AnimationSwitch animationSwitch, boolean isChecked);
    }
	
	public static final boolean CHECKED = true;
	public static final boolean UNCHECKED = false;
	
	private int mAnimationDrawableSrc = R.drawable.ios_switch;
	private int mAnimationReverseDrawableSrc = R.drawable.ios_switch_reverse;
	
	private boolean mChecked;
	private boolean mSyncMode;
	private OnCheckedChangeListener mOnCheckedChangeListener;
	
	public AnimationSwitch(Context context) {
		this(context, null);
	}
	
	public AnimationSwitch(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
		
	public AnimationSwitch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		int[] attrsArray = new int[] { android.R.attr.checked };
		TypedArray typedArray = context.obtainStyledAttributes(attrs, attrsArray);
		mChecked = typedArray.getBoolean(0, false);
		if(mChecked) {
    		setImageResource(mAnimationDrawableSrc);
    		((AnimationDrawable) getDrawable()).selectDrawable(((AnimationDrawable) getDrawable()).getNumberOfFrames() - 1);
    	} else {
    		setImageResource(mAnimationReverseDrawableSrc);
    		((AnimationDrawable) getDrawable()).selectDrawable(((AnimationDrawable) getDrawable()).getNumberOfFrames() - 1);
    	}
		typedArray.recycle();
	}

	@Override
	public boolean performClick() {
		toggle();
		playSoundEffect(SoundEffectConstants.CLICK);
		return super.performClick();
	}
	
	@Override
	public boolean isChecked() {
		return mChecked;
	}

	@Override
	public void setChecked(boolean checked) {
	    if (mChecked != checked) {
	    	mChecked = checked;
	    	
	    	if(mChecked) {
	    		setImageResource(mAnimationDrawableSrc);
	    		((AnimationDrawable)getDrawable()).start();
	    	} else {
	    		setImageResource(mAnimationReverseDrawableSrc);
	    		((AnimationDrawable)getDrawable()).start();
	    	}
	    	
	        if (mOnCheckedChangeListener != null && !mSyncMode) {
	            mOnCheckedChangeListener.onCheckedChanged(this, mChecked); 
	        }
	    }
	    if (mSyncMode) {
	    	Log.d("AnimationSwitchView", "Sync mode is deactivated");
	    }
	    mSyncMode = false;	    
	}
	
	public void setSyncMode() {
		mSyncMode = true;
		Log.d("AnimationSwitchView", "Sync mode is activated");
	}
	
	@Override
	public void toggle() {
		setChecked(!mChecked);		
	}
	
	public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
	    mOnCheckedChangeListener = listener;
	}
	
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        setChecked(savedState.checked);
        requestLayout();
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState savedState = new SavedState(superState);
		savedState.checked = isChecked();
		return savedState;
	}
	
	static class SavedState extends BaseSavedState {
		
		private boolean checked;

		public SavedState(Parcelable state) {
			super(state);
		}
		
		private SavedState(Parcel in) {
			super(in);
			checked = (Boolean)in.readValue(null);
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeValue(checked);
		}
		
		@Override
		public String toString() {
			return "CheckableAnimationImage.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " checked=" + checked + "}";
		}
		
		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

			@Override
			public SavedState createFromParcel(Parcel source) {
				return new SavedState(source);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

}
