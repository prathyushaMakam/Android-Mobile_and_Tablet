package com.prathyusha.multipaneapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class DetailFragmentActivity extends AppCompatActivity {

    private String KEY_POSITION = "position";
    private String tag = "DetailFragmentActivity";
    public static int mItemId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_fragment);

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            finish();
        }

        Log.d(tag, "before getting pothole value");


        Intent intent = this.getIntent();
        mItemId = intent.getIntExtra(MainActivity.POSITION,0);
        Log.d(tag, "after getting pothole index from main Activity:" + mItemId );


        Bundle bundle = new Bundle();
        bundle.putInt(KEY_POSITION,mItemId);
        Log.d(tag, "calling detail fragment activity now ");

        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.detailFrame, detailFragment);
        transaction.commit();
    }
}
