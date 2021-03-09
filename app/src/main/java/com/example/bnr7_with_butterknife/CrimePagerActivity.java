package com.example.bnr7_with_butterknife;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import java.util.UUID;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Class that uses a ViewPager for Crimes so it handles an UI for each crime
 */

public class CrimePagerActivity extends AppCompatActivity implements CrimeFragment.Callbacks {

    private static final String KEY_TO_CRIME="CrimeKey";

    @BindView(R.id.crimePager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        ButterKnife.bind(this);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    return CrimeFragment.newInstance(CrimeLab.getCrimeLab(CrimePagerActivity.this).getCrimes().get(position).getId());
                }

                @Override
                public int getCount() {
                    return CrimeLab.getCrimeLab(CrimePagerActivity.this).getCrimes().size();
                }
        });
        //we need to open all the crimes,not the first only
        UUID id= (UUID) getIntent().getSerializableExtra(KEY_TO_CRIME);
        for (int i=0;i<CrimeLab.getCrimeLab(this).getCrimes().size();i++){
            if (id.equals(CrimeLab.getCrimeLab(this).getCrimes().get(i).getUUID())){
                System.out.println("AND THATS FROM PAGER");
                System.out.println(CrimeLab.getCrimeLab(this).getCrimes().get(i).get_id());
                mViewPager.setCurrentItem(i);
            }
        }
    }

    /**
     *
     * @param context needs for intent
     * @param id shows us which crime we need to setup
     * @return the intent we receive
     */
    public static Intent newIntent(Context context, UUID id){
        Intent intent=new Intent(context,CrimePagerActivity.class);
        intent.putExtra(KEY_TO_CRIME,id);
        return intent;
    }

    /**
     * We dont need callbacks of this activity
     */
    @Override
    public void onDetailUpdated() {
    }

    /**
     * We dont need callbacks of this activity
     */
    @Override
    public void onDetailDeleted() {
        this.finish();
    }
}