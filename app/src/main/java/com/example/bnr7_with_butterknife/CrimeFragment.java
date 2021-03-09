package com.example.bnr7_with_butterknife;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

/**
 * Fragment that cares all UI of details of Crime
 */

public class CrimeFragment extends Fragment {


    //Key for intents
    private static final String KEY_TO_ARGS="KeyToArgs";
    private static final int KEY_TO_TARGETING_FRAGMENT_DATE=0;
    private static final int KEY_TO_TARGETING_FRAGMENT_TIME=1;
    private static final int KEY_TO_TARGETING_FRAGMENT_SUSPECT=2;
    private static final int KEY_TO_TARGETING_PHOTO=3;


    //Activity that we need to use for CallBack
    private Callbacks mActivityCallback;

    //Crime that we display
    private Crime mCrime;

    //File with image of crime
    private File mPhotoFile;

    //Intent that we can use for camera
    //Init here because we also use for permission resolving
    private Intent camIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    //we need an Unbinder because of Fragments Lifecycle
    private Unbinder unbinder;

    @BindView(R.id.photoImage) ImageView mPhotoView;
    @BindView(R.id.CheckBox) CheckBox mSolvedCheck;
    @BindView(R.id.editText) EditText mTextTitle;
    @BindView(R.id.crime_date_button) Button mDateButton;
    @BindView(R.id.crime_time_button) Button mTimeButton;
    @BindView(R.id.suspect_button) Button mSuspectButton;
    @BindView(R.id.send_report) Button mSendReportButton;
    @BindView(R.id.take_photo_btn) ImageButton mPhotoImageButton;

    @OnClick(R.id.photoImage)
    public void onClickPhotoImage(View v){
        ImageDialog imageDialog=ImageDialog.newInstance(mPhotoFile.getPath());
        imageDialog.show(getFragmentManager(),null);
    }

    @OnClick(R.id.take_photo_btn)
    public void onClickPhoto(View v){
        Uri uri=FileProvider.getUriForFile(getActivity(),"com.example.bnr7_with_butterknife.photoauthority",mPhotoFile);
        camIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        List<ResolveInfo> promList=getActivity().getPackageManager().queryIntentActivities(camIntent,PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo info:promList){
            getActivity().grantUriPermission(info.activityInfo.packageName,uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        startActivityForResult(camIntent,KEY_TO_TARGETING_PHOTO);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mActivityCallback= (Callbacks) context;
    }

    public void onDetach(){
        super.onDetach();
        mActivityCallback=null;
    }

    @OnClick(R.id.send_report)
    public void onClickSendReport(View v){
        ShareCompat.IntentBuilder.from(getActivity())
                .setType("type/plain")
                .setChooserTitle(getResources().getString(R.string.send_report_via))
                .setText(getCrimeReport())
                .startChooser();
    }

    @OnClick(R.id.suspect_button)
    public void onClickSuspect(View v){
        Intent intent=new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent,KEY_TO_TARGETING_FRAGMENT_SUSPECT);
    }

    @OnClick(R.id.crime_time_button)
    public void onClickTime(View v){
        TimePickerFragment fragment= TimePickerFragment.newInstance(mCrime.getDate());
        fragment.setTargetFragment(this,KEY_TO_TARGETING_FRAGMENT_TIME);
        fragment.show(getFragmentManager(),null);
    }


    @OnClick(R.id.crime_date_button)
    public void onClickDate(View v){
        /**
         * @see R.bool.isTablet
         * on the small screens we obviously want to start new Activity rather then start a FragmentDialog
         * so it helps us to get knowledge about the screen size
         */
        if (!getResources().getBoolean(R.bool.isTablet)){
            //startActivity
            startActivityForResult(DatePickerActivity.newInstance(getActivity(),mCrime.getDate()),KEY_TO_TARGETING_FRAGMENT_DATE);
        }
        else {
            //start a FragmentDialog
            DatePickerFragment fragment = DatePickerFragment.newInstance(mCrime.getDate());
            fragment.setTargetFragment(this, KEY_TO_TARGETING_FRAGMENT_DATE);
            fragment.show(getFragmentManager(), null);
        }

    }

    /**
     * @see android.widget.RadioGroup.OnCheckedChangeListener
     * Butterknife is about to give all of UI elemnts a listener
     */
    @OnCheckedChanged(R.id.CheckBox)
    public void onCheckedChanged(boolean isChecked){
        mCrime.setSolved(isChecked);
        updateCrimeState();
    }

    @OnTextChanged(R.id.editText)
    public void onTextChanged(CharSequence s){
        mCrime.setTitle(s.toString());
        updateCrimeState();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_crime:{
                //delete selected crime and get back to crimeListActivity
                CrimeLab.getCrimeLab(getActivity()).deleteCrime(mCrime);
                updateCrimeState();
                mActivityCallback.onDetailDeleted();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.crime_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //declaring a toolbar
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        //ButterKnife.bind(this, v);//so important to set it before the init of other Views
        unbinder = ButterKnife.bind(this, v);

        //display info without any redaction
        mSolvedCheck.setChecked(mCrime.isSolved());
        mTextTitle.setText(mCrime.getTitle());
        if ((getActivity().getPackageManager().resolveActivity(new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI),PackageManager.MATCH_DEFAULT_ONLY)) == null) mSuspectButton.setEnabled(false);
        boolean canTakePhoto=(camIntent.resolveActivityInfo(getActivity().getPackageManager(),PackageManager.MATCH_DEFAULT_ONLY)!=null);
        mPhotoImageButton.setEnabled(canTakePhoto);
        updateDate();

        /**
         * It Helps us to scale image correctly
         */
        ViewTreeObserver observer=mPhotoView.getViewTreeObserver();
        if (observer.isAlive()){
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mPhotoView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    updateImage(mPhotoView.getMeasuredWidth(),mPhotoView.getMeasuredHeight());
                }
            });
        }
        return v;
    }

    /**
     * Creates a report that we can send via smth
     * @return report String
     */
    private String getCrimeReport(){
        String date=DateFormat.format("EEE, yyyy/mm/dd,HH:mm",mCrime.getDate()).toString();
        String title=mCrime.getTitle();
        String suspect=(mCrime.getSuspect()!=null ? getActivity().getResources().getString(R.string.crime_suspect_found_report,mCrime.getSuspect()) : getActivity().getResources().getString(R.string.crime_suspect_not__found_report));
        String solved=(mCrime.isSolved() ? getActivity().getResources().getString(R.string.crime_solved_report) : getActivity().getResources().getString(R.string.crime_unsolved_report));
        return getString(R.string.crime_report,title,date,suspect,solved);
    }

    /**
     * sets both date and time
     */
    private void updateDate() {
        //mDateButton.setText(DateFormat.format("EEEE, yyyy/MM/dd HH:mm",mCrime.getDate()));
        mDateButton.setText(java.text.DateFormat.getDateInstance().format(mCrime.getDate()));
        //java.text.DateFormat df= java.text.DateFormat.getDateInstance().format(mCrime.getDate());
        updateCrimeState();
    }

    /**
     * Updates image
     */
    private void updateImage(int width,int height) {
        if (mPhotoFile==null || !mPhotoFile.exists()) {
            mPhotoView.setImageBitmap(null);
        }
        else if (mPhotoFile.exists()) {
            mPhotoView.setImageBitmap(PhotoUltility.decodeBitmap(mPhotoFile.getPath(),width,height));
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        CrimeLab.getCrimeLab(getActivity()).updateCrime(mCrime);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //take id of Crime that needed from intent
        super.onCreate(savedInstanceState);
        mCrime = CrimeLab.getCrimeLab(getActivity()).getCrimeById((UUID) getArguments().getSerializable(KEY_TO_ARGS));
        mPhotoFile=CrimeLab.getCrimeLab(getActivity()).getPhotoFile(mCrime);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * {@link DatePickerFragment} and {@link TimePickerFragment} used to call this method
     * To let us know the updates user make
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode!=Activity.RESULT_OK) return;
        switch (requestCode) {
            case KEY_TO_TARGETING_FRAGMENT_DATE:{
                mCrime.setDate((Date) data.getSerializableExtra(DatePickerFragment.KEY_TO_EXTRA_DATE));
                updateDate();
                updateCrimeState();
                break;
            }
            case KEY_TO_TARGETING_FRAGMENT_TIME:{
                mCrime.setDate((Date) data.getSerializableExtra(TimePickerFragment.KEY_TO_EXTRA_TIME));
                updateDate();
                updateCrimeState();
                break;
            }
            case KEY_TO_TARGETING_FRAGMENT_SUSPECT:{
                Uri uri=data.getData();
                String []clause={ContactsContract.Contacts.DISPLAY_NAME};
                Cursor cursor=getActivity().getContentResolver().query(uri,clause,null,null,null);
                if (cursor.getCount()!=0){
                    try {
                        cursor.moveToFirst();
                        mCrime.setSuspect(cursor.getString(0));
                        updateCrimeState();
                        mSuspectButton.setText(mCrime.getSuspect());
                    }
                    finally {
                        cursor.close();
                    }
                }
            }
            case KEY_TO_TARGETING_PHOTO:{
                Uri uri=FileProvider.getUriForFile(getActivity(),"com.example.bnr7_with_butterknife.photoauthority",mPhotoFile);
                getActivity().revokeUriPermission(uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                updateImage(mPhotoView.getMeasuredWidth(),mPhotoView.getMaxHeight());
                updateCrimeState();
            }
        }
    }

    /**
     * Updates the {@CrimeListFragment}
     */
    private void updateCrimeState(){
        CrimeLab.getCrimeLab(getActivity()).updateCrime(mCrime);
        mActivityCallback.onDetailUpdated();
    }

    /**
     *
     * @param id shows us which Crime we need to put into fragment
     * @return the fragment to show
     */
    public static Fragment newInstance(UUID id){
        Bundle args=new Bundle();
        args.putSerializable(KEY_TO_ARGS,id);
        CrimeFragment fragment=new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Interface for Activity usage
     */
    public interface Callbacks{
        void onDetailUpdated();
        void onDetailDeleted();
    }
}