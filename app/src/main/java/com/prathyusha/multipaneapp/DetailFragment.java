package com.prathyusha.multipaneapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DetailFragment extends Fragment {

    final static String KEY_POSITION = "position";
    static int mCurrentPosition = -1;

    static String[] mPotholeDetail;
    static TextView mPotholeDetailTextView;


    public DetailFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPotholeDetail = getResources().getStringArray(R.array.version_descriptions);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null){
            mCurrentPosition = savedInstanceState.getInt(KEY_POSITION);
        }


        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        mPotholeDetailTextView = (TextView) view.findViewById(R.id.pothole_detail);
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

        // During the startup, we check if there are any arguments passed to the fragment.
        // onStart() is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method below
        // that sets the description text
        Bundle args = getArguments();
        if (args != null){
            // Set description based on argument passed in
            setDetails(args.getInt(KEY_POSITION));
        } else if(mCurrentPosition != -1){
            // Set description based on savedInstanceState defined during onCreateView()
            setDetails(mCurrentPosition);
        }
    }

    public static void setDetails(int detailIndex){
        mPotholeDetailTextView.setText(mPotholeDetail[detailIndex]);
        mCurrentPosition = detailIndex;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current description selection in case we need to recreate the fragment
        outState.putInt(KEY_POSITION,mCurrentPosition);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
