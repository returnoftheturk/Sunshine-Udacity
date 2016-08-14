package com.example.android.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by akgul on 7/27/2016.
 */

public class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }

    public ArrayAdapter<String> myForecastAdapter;

    final String LOG_TAG = ForecastFragment.class.getSimpleName();
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.v(LOG_TAG,"ONCREATE" );

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.action_refresh){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateWeather(){
        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity() ,myForecastAdapter);


//        String format = null;
//        String units = null;
//        String dayCount = null;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String postalCode = sharedPreferences.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
        //units = sharedPreferences.getString(getString(R.string.pref_units_key), getString(R.string.pref_units_default));
        //Log.v("Error","UNITS:  " + units );
//        format = "json";
//        units = "metric";
//        dayCount = "7";

        weatherTask.execute(postalCode);

    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
        Log.v(LOG_TAG,"ONSTART");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(LOG_TAG,"ONPAUSE");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(LOG_TAG,"ONSTOP");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(LOG_TAG,"ONRESUME");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG,"ONDESTROY");
    }
    /* The date/time conversion code is going to be moved outside the asynctask later,
         * so for convenience we're breaking it out into its own method now.
         */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        myForecastAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_forecast, R.id.list_item_forecast_textview, new ArrayList<String>());


        ListView listView = (ListView)rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(myForecastAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getActivity(), myForecastAdapter.getItem(i), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                    .putExtra(Intent.EXTRA_TEXT, myForecastAdapter.getItem(i));
                startActivity(intent);
            } 
        }
        );

        return rootView;
    }


}
