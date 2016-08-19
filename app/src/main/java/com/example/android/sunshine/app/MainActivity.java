package com.example.android.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity implements ForecastFragment.Callback{
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    public String mLocation;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;

    public void onItemSelected(Uri uri){
        if (mTwoPane){
//            DetailFragment detailFragment = DetailFragment.newInstance(uri);

            Bundle bundle = new Bundle();
            bundle.putParcelable(DetailFragment.DETAIL_URI, uri);
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(bundle);


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.weather_detail_container, detailFragment, DETAILFRAGMENT_TAG)
                    .commit();
        }
        else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.setData(uri);
            startActivity(intent);

        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLocation = Utility.getPreferredLocation(this);


        if(findViewById(R.id.weather_detail_container)!=null){
            mTwoPane = true;

            if(savedInstanceState==null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else{
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }
        ForecastFragment forecastFragment = (ForecastFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);

        forecastFragment.setmUseToday(!mTwoPane);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String location = Utility.getPreferredLocation( this );
        // update the location in our second pane using the fragment manager
        if (location != null && !location.equals(mLocation)) {
            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
            if ( null != ff ) {
                ff.onLocationChanged();
            }
            DetailFragment df = (DetailFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
            if ( null != df ) {
                df.onLocationChanged(location);
            }
            mLocation = location;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        if (id == R.id.action_map){
            openLocationOnMap();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openLocationOnMap(){
        Intent intent = new Intent(Intent.ACTION_VIEW);


        String location = Utility.getPreferredLocation(this);
        Uri uri = Uri.parse("geo:0,0?")
                .buildUpon().appendQueryParameter("q", location).build();
        intent.setData(uri);

        if (intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }
        else Log.d(LOG_TAG, "Couldn't open location: " + location + "no map application installed");

    }


}
