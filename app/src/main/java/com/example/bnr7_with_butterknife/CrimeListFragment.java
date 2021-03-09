package com.example.bnr7_with_butterknife;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment that contains a RecyclerView with an adapter&VH
 * Also checks and updates info about crimes that user made in {@link CrimeActivity}
 */

public class CrimeListFragment extends Fragment {
    @BindView(R.id.crimeRecyclerView)
    RecyclerView mCrimeRecyclerView;

    @BindView(R.id.emptyText)
    TextView mEmptyTextView;

    private Unbinder unbinder;
    private boolean mIsSubtitleShown=false;
    //Bundle key
    private static final String KEY_TO_TITLEVIS="titlevisState";

    //Activity that we need to use for CallBack
    private Callbacks mActivityCallback;

    //RecyclerView Adapter
    private CrimeAdapter mCrimeAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivityCallback= (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityCallback=null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //save the subtitle state when configuration is changing
        if (savedInstanceState!=null){
            mIsSubtitleShown=savedInstanceState.getBoolean(KEY_TO_TITLEVIS);
        }

        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        /**
         * Provides swipe-delete
         */
        ItemTouchHelper helper=new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int vertFlags=ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int horFlags = ItemTouchHelper.RIGHT;
                return makeMovementFlags(vertFlags,horFlags);
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    CrimeHolder holder = (CrimeHolder) viewHolder;
                    mCrimeAdapter.mCrimes.remove(viewHolder.getAdapterPosition());
                    CrimeLab.getCrimeLab(getActivity()).deleteCrime(holder.mCrime);
                    mCrimeAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    updateEmptyTitleVis();
                    mActivityCallback.onDetailDeleted();
            }
        });
        helper.attachToRecyclerView(mCrimeRecyclerView);
        setHasOptionsMenu(true); //Toolbar sign
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_TO_TITLEVIS,mIsSubtitleShown);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.list_menu,menu);
        MenuItem item=menu.findItem(R.id.subtitle);
        item.setTitle(mIsSubtitleShown ? getResources().getString(R.string.hide_subtitle) : getResources().getString(R.string.show_subtitle));
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.addCrime:{
                //Intent intent = CrimePagerActivity.newIntent(getActivity(),CrimeLab.getCrimeLab(getActivity()).addCrime().getId());
                //startActivity(intent);
                updateUI();
                mActivityCallback.onItemSelected(CrimeLab.getCrimeLab(getActivity()).addCrime());
                return true;
            }

            case R.id.subtitle:{
                //changes the subtitle vis and text
                mIsSubtitleShown=!mIsSubtitleShown;
                updateTitle();
                getActivity().invalidateOptionsMenu();
                return true;
            }

            default: {return super.onOptionsItemSelected(item);}
        }
    }

    /**
     * Updates Title of list whenever {@Crime} was added or deleted
     */
    private void updateEmptyTitleVis(){
        if (CrimeLab.getCrimeLab(getActivity()).getCrimes().size()!=0)
            mEmptyTextView.setVisibility(TextView.GONE);
        else mEmptyTextView.setVisibility(TextView.VISIBLE);
    }

    /**
     * Updates subtitle state
     */
    private void updateTitle(){
        String title=getResources().getQuantityString(R.plurals.crime_format,CrimeLab.getCrimeLab(getActivity()).getCrimes().size(),CrimeLab.getCrimeLab(getActivity()).getCrimes().size());
        if (!mIsSubtitleShown) title=null;
        AppCompatActivity activity= (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(title);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI(); //to update the info right after the user clicked "back" on CrimeFragment
        updateEmptyTitleVis();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * Updates Adapters list of Crimes whenever its updated
     */
    public void updateUI(){
        if (mCrimeAdapter==null) {
            mCrimeAdapter = new CrimeAdapter(CrimeLab.getCrimeLab(getActivity()).getCrimes());
            mCrimeRecyclerView.setAdapter(mCrimeAdapter);
        }
        else {
            List<Crime> crimes=CrimeLab.getCrimeLab(getActivity()).getCrimes();
            mCrimeAdapter.setCrimes(crimes);
            mCrimeAdapter.notifyDataSetChanged();
        }
        updateEmptyTitleVis();
    }

    //so cool Kappa
    public static CrimeListFragment newInstance(){
        CrimeListFragment fragment=new CrimeListFragment();
        return fragment;
    }


    /**
     * CrimeHolder for the RecyclerView from this class
     * @see {R.layout.crime_list_item.xml}
     */
    class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public Crime getCrime() {
            return mCrime;
        }

        //binds another crime every time
        private Crime mCrime;

        @BindView(R.id.CrimeTitle)
        TextView mCrimeTitle;
        @BindView(R.id.CrimeDate)
        TextView mCrimeDate;
        @BindView(R.id.crime_solved)
        ImageView mCrimeSolved;

        public CrimeHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
            //so we can touch our Holders
            itemView.setOnClickListener(this);
           //butterknife needs the same View that we used for a super();
        }

        //go for details of crime
        @Override
        public void onClick(View v) {
            mActivityCallback.onItemSelected(mCrime);
        }

        /**
         * sets params for each crime in list
         * @param crime crime that include params we need
         */
        private void bind(Crime crime){
            mCrime = crime;
            mCrimeTitle.setText(mCrime.getTitle());
            mCrimeDate.setText(DateFormat.format("yyyy/MM/dd HH:mm",mCrime.getDate()));
            mCrimeSolved.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
        }
    }

    private class  CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{

        private List<Crime> mCrimes;
        public CrimeAdapter(List<Crime> crimes) {
            setCrimes(crimes);
        }

        public void setCrimes(List<Crime> crimes){
            this.mCrimes=crimes;
        }

        public List<Crime> getCrimes() {
            return mCrimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v=LayoutInflater.from(getActivity()).inflate(R.layout.crime_list_item,parent,false);
            CrimeHolder holder=new CrimeHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            holder.bind(CrimeLab.getCrimeLab(getActivity()).getCrimes().get(position));
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

    public interface Callbacks{
        void onItemSelected(Crime crime);
        void onDetailDeleted();
    }
}