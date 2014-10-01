package com.wimoto.app.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobitexoft.widgets.RangeBar;
import com.mobitexoft.widgets.RangeBar.OnRangeBarChangeListener;
import com.wimoto.app.R;

public class AlarmSliderDialog extends AlertDialog.Builder {

	public AlarmSliderDialog(Context context) {
		super(context);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rangeView = inflater.inflate(R.layout.range_view, null);
		final TextView resultTextView = (TextView)rangeView.findViewById(R.id.resultTextView);
		
		RangeBar<Float> seekBar = new RangeBar<Float>(0.0f, 100.0f, context);
		seekBar.setSelectedMinValue(50.0f);
		seekBar.setSelectedMaxValue(90.0f);
		seekBar.setOnRangeBarChangeListener(new OnRangeBarChangeListener<Float>() {
		        @Override
		        public void onRangeBarValuesChanged(RangeBar<?> bar, Float minValue, Float maxValue) {
		                resultTextView.setText(String.format("%.2f - %.2f", minValue, maxValue));
		        }
		});
		
		resultTextView.setText(String.format("%.2f - %.2f", seekBar.getSelectedMinValue(), seekBar.getSelectedMaxValue()));

		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(60, 40, 60, 60);
		seekBar.setLayoutParams(params);
		
		LinearLayout layout = (LinearLayout)rangeView.findViewById(R.id.rangeView);
		layout.addView(seekBar);
		
		setCancelable(false);
		setPositiveButton(context.getString(R.string.sensor_save),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    	save();
                        dialog.cancel();
                    }
                });
        setNegativeButton(context.getString(R.string.sensor_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

		setView(rangeView);
	}

	
	private void save() {
		
	}
}
