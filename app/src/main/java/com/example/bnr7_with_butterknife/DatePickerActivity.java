package com.example.bnr7_with_butterknife;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import java.util.Date;

/**
 * Activity which starting when we go on a phones (small screen)
 * provides all the stuff that  {@link DatePickerFragment} can do
 */

public class DatePickerActivity extends SingleFragmentActivity {

    public static final String KEY_TO_DATE_INTENT="key_to_date_intent";

    /**
     * Creates a {@DatePickerFragment} and puts into args of fragment date we suppose to show
     * @return DatePickerFragment
     */
    @Override
    protected Fragment createFragment() {
        return DatePickerFragment.newInstance((Date) getIntent().getSerializableExtra(KEY_TO_DATE_INTENT));
    }

    public static Intent newInstance(Context context, Date date){
        Intent intent=new Intent(context,DatePickerActivity.class);
        intent.putExtra(KEY_TO_DATE_INTENT,date);
        return intent;
    }

}
