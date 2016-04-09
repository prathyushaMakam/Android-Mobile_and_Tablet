package com.prathyusha.multipaneapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements OnPotholeSelectionChanged {

    public final static String POSITION ="position";
    private String tag="mainActivity";
    private int lastPosition = -1;
    Bundle element = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null){
            int restorePosition = savedInstanceState.getInt("position");
            if(restorePosition != -1)
            {
                OnItemChanged(restorePosition);
            }
        }

    }

    @Override
    public void OnItemChanged(int potholeIndex) {

        this.lastPosition = potholeIndex;
        DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment);

        if (detailFragment == null)
        {
            Log.d(tag, "inside onSelectionChanged: " + potholeIndex);

            Intent intent = new Intent(this,DetailFragmentActivity.class);
            Bundle element = new Bundle();
            element.putInt("position",potholeIndex);
            intent.putExtra("bundle",element);
            startActivity(intent);
        }
        else {
            // If description is available, we are in two pane layout
            // so we call the method in DescriptionFragment to update its content
            DetailFragment.setDetails(potholeIndex);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle oustState)
    {
        oustState.putInt("position", lastPosition);
    }


    @Override

    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() != 0) {

            getFragmentManager().popBackStack();

        } else {

            super.onBackPressed();

        }

    }
}
