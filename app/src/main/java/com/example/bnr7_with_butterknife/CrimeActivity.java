package com.example.bnr7_with_butterknife;

import android.content.Intent;

import androidx.fragment.app.Fragment;

public class CrimeActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks,CrimeFragment.Callbacks {

    /**
     * Activity that cares {@link CrimeListFragment}
     * when any of crimes from got clicked,the fragment inside sends an intent to {@link CrimePagerActivity}
     */

    /**
     * Fucntion that helps us choose Fragment for target SingleFragmentActivity
     * {@CrimeListFragment is RecyclerView with a list of crimes}
     * @return New {@link CrimeListFragment}
     */
    @Override
    protected Fragment createFragment() {
        return CrimeListFragment.newInstance();
    }


    /**
     * Method from {@link CrimeListFragment.Callbacks}
     * Manage whether we start new activity or new fragment (on Big screens)
     * @param crime crime we need to show in {@link CrimeFragment}
     */
    @Override
    public void onItemSelected(Crime crime){
        if (findViewById(R.id.fragment_detail_container)==null){
            Intent intent=CrimePagerActivity.newIntent(this,crime.getId());
            startActivity(intent);
        }
        else{
            Fragment fragment=CrimeFragment.newInstance(crime.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_container,fragment)
                    .commit();
        }
    }


    /**
     * Updates the {@CrimeListFragment} whenever we do something with Crime on big screen
     */
    @Override
    public void onDetailUpdated() {
        CrimeListFragment fragment= (CrimeListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        fragment.updateUI();
    }

    /**
     * Deletes the detail {@CrimeFargment} fragment when we delete a crime on big screen
     *
     */
    @Override
    public void onDetailDeleted() {
        if (findViewById(R.id.fragment_detail_container)!=null) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_detail_container);
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }
}