package com.prathyusha.multipaneapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnPotholeSelectionChanged {

    public final static String POSITION ="position";
    private String tag="mainActivity";
    private int lastPosition = -1;
    Bundle element = new Bundle();

    public final static String POTHOLE_ID ="PotholeID";
    public final static String ITEM_ID ="ItemID";

    public static ArrayList<Integer> itemsId  = new ArrayList<Integer>();
    public static ArrayList<String> mPotholeIdValue = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(tag, "inside On create");


        //GetList();


        if(getFragmentManager().findFragmentById(R.id.fragment_container) == null) {

            Log.d(tag, "inside fragment manager");

            if (savedInstanceState != null){
                int restorePosition = savedInstanceState.getInt("position");
                if(restorePosition != -1)
                {
                    OnItemChanged(restorePosition);
                }
            }

        }
    }

    private void GetList() {
        // Create request queue
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        Log.d(tag, "inside On getList");

        //  Create json array request
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.POST,"http://bismarck.sdsu.edu/city/batch?type=street&user=rew&size=10&batch-number=0&end-id=15",new Response.Listener<JSONArray>(){
            public void onResponse(JSONArray jsonArray){
                // Successfully download json
                // So parse it and populate the listview
                for(int i=0;i<jsonArray.length();i++){
                    try {
                        Log.d(tag, "inside JsonArray try");

                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        itemsId.add(i);
                        mPotholeIdValue.add(jsonObject.getString("id"));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                for (int i=0; i<itemsId.size();i++) {

                    Log.d(tag, "Inside Volley,  itemsId :" + itemsId.get(i) + "Pothole Value: "+ mPotholeIdValue.get(i));
                }

                Log.d(tag, "after json objects");

                PotholeListFragment listFragment = new PotholeListFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.fragment_container,listFragment);
                fragmentTransaction.commit();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(tag, "inside JsonArray error response");

                Log.e("Error", "Unable to parse json array");
            }
        });

        // add json array request to the request queue
        requestQueue.add(jsonArrayRequest);

    }

    @Override
    public void OnItemChanged(int potholeIndex) {

        for (int i=0; i<itemsId.size();i++) {

            Log.d(tag, "itemsId :" + itemsId.get(i) + "Pothole Value: "+ mPotholeIdValue.get(i));
        }

        this.lastPosition = potholeIndex;
        DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment);

        if (detailFragment == null)
        {
            Log.d(tag, "inside onSelectionChanged: " + potholeIndex);

            Intent intent = new Intent(this,DetailFragmentActivity.class);
           // Bundle element = new Bundle();
            Log.d(tag,"sending potholeIndex to detailActivity");

            intent.putExtra(POSITION, potholeIndex);
            Log.d(tag, "sent pothole index to detail Activity & before starting intent");

            // intent.putExtra("bundle",element);
            startActivity(intent);
            Log.d(tag, "sent pothole index to detail Activity after starting intent");

        }
        else {
            // If description is available, we are in two pane layout
            // so we call the method in DescriptionFragment to update its content
            Log.d(tag,"before sending changed pothole index when detailfragment != null");
            detailFragment.setDetails(potholeIndex);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle oustState) {
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
