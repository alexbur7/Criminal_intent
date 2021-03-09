package com.example.bnr7_with_butterknife;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Class that helps user to choose the date of Crime (presents at {@link CrimeListFragment})
 * Target fragment is {@link CrimeFragment}
 */

public class DatePickerFragment extends DialogFragment {

    //Key for args
    private static final String KEY_TO_DATE="keyDate";

    //Intent key for onActivityResult(int,int,Intent)
    public static final String KEY_TO_EXTRA_DATE="keyExtraDate";

    private View mView;

    @BindView(R.id.datePicker) DatePicker mDatePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // we init the fragment in onCreateView so we can decide create an AlertDialog or not
        if (!getResources().getBoolean(R.bool.isTablet)) {
            Calendar calendar = InitializeCalendar();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            mDatePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    sendResult(Activity.RESULT_OK, new GregorianCalendar(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth()).getTime());
                }
            });
            return mView;
        }
        else return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * Inits the calendar,same both on big and small screens
     * @return
     */
    private Calendar InitializeCalendar() {
        Date date = (Date) getArguments().get(KEY_TO_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
        ButterKnife.bind(this, mView);
        return calendar;
    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getResources().getBoolean(R.bool.isTablet)) {
            Calendar calendar = InitializeCalendar();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            mDatePicker.init(year,month,day,null);
            return new AlertDialog.Builder(getActivity())
                    .setView(mView)
                    .setTitle(R.string.date_picker_title)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendResult(Activity.RESULT_OK, new GregorianCalendar(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth()).getTime());
                        }
                    })
                    .create();
        }
        else return super.onCreateDialog(savedInstanceState);
    }

    /**
     * send results back to {@CrimeFragment}
     * @param resultCode
     * @param date
     */
    private void sendResult(int resultCode,Date date){
        Intent intent=new Intent();
        intent.putExtra(KEY_TO_EXTRA_DATE,date);
        if (getTargetFragment()==null) getActivity().setResult(resultCode,intent);
        else getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }

    public static DatePickerFragment newInstance(Date date){
        Bundle bundle=new Bundle();
        bundle.putSerializable(KEY_TO_DATE,date);
        DatePickerFragment fragment=new DatePickerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
