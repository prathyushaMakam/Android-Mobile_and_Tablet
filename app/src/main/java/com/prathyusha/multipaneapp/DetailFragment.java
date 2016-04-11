package com.prathyusha.multipaneapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DetailFragment extends Fragment {

    final static String KEY_POSITION = "position";
    static int mCurrentPosition = -1;

    private static String mPotholeUserId;
    private static String mPotholeDescription;
    private static String mImageType;

    private static String tag ="DetailFragment";
    private int mItemId = DetailFragmentActivity.mItemId ;
    static TextView mPotholeDetailTextView ;
    private static ImageView mPotholeImage;


    public DetailFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null){
            mCurrentPosition = savedInstanceState.getInt(KEY_POSITION);
        }


        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        mPotholeDetailTextView = (TextView) view.findViewById(R.id.pothole_detail);
        mPotholeImage = (ImageView) view.findViewById(R.id.PotholeImage);
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

    public  void setDetails(final Integer detailIndex) {

        mCurrentPosition = detailIndex;

        Log.d(tag, "pothole Index value:" + detailIndex);


        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
        //  Create json array request
        Log.d(tag, "After Json requestQueue");
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.POST, "http://bismarck.sdsu.edu/city/batch?type=street&user=rew&size=10&batch-number=0&end-id=15",new Response.Listener<JSONArray>(){
            public void onResponse(JSONArray jsonArray){
                Log.d(tag,"Inside onResponse");

                // Successfully download json
                // So parse it and populate the listview
                try {
                    JSONObject Id = jsonArray.getJSONObject(detailIndex);
                    JSONObject Description=jsonArray.getJSONObject(detailIndex);
                    JSONObject ImageType=jsonArray.getJSONObject(detailIndex);

                    Log.d(tag, "After Json Object declaration");


                    mPotholeUserId = Id.getString("id");
                    mPotholeDescription=Description.getString("description");
                    mImageType = ImageType.getString("imagetype");

                    Log.d(tag, "After getting potholeUserId:" + mPotholeUserId + " :Description :" + mPotholeDescription +" :ImageType :"+ mImageType);

                    mPotholeDetailTextView.setText(mPotholeDescription);

                    if(!mImageType.equals("none") )
                    {
                        Log.d(tag,"Image type is not None");
                        DisplayImage();
                    }
                    Log.d(tag, "mPotholeIdValue[" + detailIndex + "]= "+ mPotholeUserId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            // }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Error", "Unable to parse json array");
            }
        });
        // add json array request to the request queue
        requestQueue.add(jsonArrayRequest);
    }

    public void DisplayImage() {
        Response.Listener<Bitmap> success = new Response.Listener<Bitmap>() {
            public void onResponse(Bitmap response) {
                 mPotholeImage.setImageBitmap(response);

            }
        };
        Response.ErrorListener failure = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("rew", error.toString());
            }
        };
        String url = "http://bismarck.sdsu.edu/city/image?id="+mPotholeUserId;
        ImageRequest ir = new ImageRequest(url,
                success, 0, 0, ImageView.ScaleType.CENTER_INSIDE, null, failure);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(ir);
    }



        @Override
        public void onSaveInstanceState (Bundle outState){
            super.onSaveInstanceState(outState);

            // Save the current description selection in case we need to recreate the fragment
            outState.putInt(KEY_POSITION, mCurrentPosition);
        }

        @Override
        public void onDetach () {
            super.onDetach();
        }


}
