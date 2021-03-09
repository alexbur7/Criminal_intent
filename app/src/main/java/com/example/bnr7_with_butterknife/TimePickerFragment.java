package com.example.bnr7_with_butterknife;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment that helps us to set a time of Crime
 * The time sets in a Date in after works like {@link DatePickerFragment}
 */

public class TimePickerFragment extends DialogFragment {

    public static final String KEY_TO_EXTRA_TIME="key_to_time";
    private static final String KEY_TO_ARGS="key_to_time_args";

    @BindView(R.id.timePicker)
    TimePicker mTimePicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime((Date) getArguments().get(KEY_TO_ARGS));

        View v= LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time,null);
        ButterKnife.bind(this,v);
        //we need to remain year,month & day correct
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);

        //init the timepicker
        mTimePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        mTimePicker.setMinute(calendar.get(Calendar.MINUTE));

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.time_picker_title)
                .setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK,new GregorianCalendar(year,month,day,mTimePicker.getHour(),mTimePicker.getMinute()).getTime());
                    }
                })
                .create();
    }

    private void sendResult(int resultCode,Date date){
        Intent intent=new Intent();
        intent.putExtra(KEY_TO_EXTRA_TIME,date);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }

    public static TimePickerFragment newInstance(Date date){
        TimePickerFragment fragment=new TimePickerFragment();
        Bundle args=new Bundle();
        args.putSerializable(KEY_TO_ARGS,date);
        fragment.setArguments(args);
        return fragment;
    }
}