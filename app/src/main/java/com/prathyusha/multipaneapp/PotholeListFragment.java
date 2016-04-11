package com.prathyusha.multipaneapp;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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


public class PotholeListFragment extends ListFragment {

    private ArrayList<Integer> itemsId  = new ArrayList<Integer>();
    private ArrayList<String> itemsName = new ArrayList<String>();
    private ArrayList<String> mPotholeIdValue = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    public static int ItemId;

    private String tag ="PotholeListFragment";

    public PotholeListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(tag, "inside list Fragment OnCreate ");

        GetList();

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(tag, "inside onActivity Created list Fragment ");

        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, itemsName);
        setListAdapter(adapter);

        for(int i=0; i<itemsId.size();i++)
        {
            Log.d(tag, itemsName.get(i) + ": " + mPotholeIdValue.get(i) + "for i :" + i);
        }

    }

    private void GetList() {
        // Create request queue
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
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
                        itemsName.add("Pothole " + itemsId.get(i));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                for (int i=0; i<itemsId.size();i++) {

                    Log.d(tag, "Inside Volley,  itemsId :" + itemsId.get(i) + "Pothole Value: "+ mPotholeIdValue.get(i));
                }

                adapter.notifyDataSetChanged();

                Log.d(tag, "after json objects");


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
    public void onListItemClick(ListView l, View v, int position, long id) {
        OnPotholeSelectionChanged listener = (OnPotholeSelectionChanged) getActivity();
        ItemId = position;
        Log.d(tag,"onListItem Changed its position: "+ position);

        listener.OnItemChanged(position);
    }
}
