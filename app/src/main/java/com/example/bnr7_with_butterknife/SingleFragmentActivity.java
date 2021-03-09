package com.example.bnr7_with_butterknife;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * Abstract Class for (OMG!) Activities with a single fragment
 * @see CrimeActivity
 * @see DatePickerActivity
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

    /**
     * Chooser which Fragment we need to show in Activity
     * @return Fragment we needed
     */
    protected abstract Fragment createFragment();

    /**
     *
     * @return reference for the ActivityFragment we needed
     */
    @LayoutRes
    protected int getLayoutId(){
        return R.layout.detail_master;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        FragmentTransaction fm=getSupportFragmentManager().beginTransaction();
        Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment==null) {
            fragment = createFragment();
            fm.add(R.id.fragment_container, fragment);
            fm.commit();
        }
    }
}
