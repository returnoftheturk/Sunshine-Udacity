package com.example.android.sunshine.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sunshine.app.data.WeatherContract;

/**
 * Created by Owner on 8/17/2016.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private final String LOG_TAG = DetailFragment.class.getSimpleName();
    public final String SUNSHINE_HASHTAG = " #SunshineApp";
    public String mDailyForecast;
    private ShareActionProvider mShareActionProvider;
    private Uri mUri;
    static final String DETAIL_URI = "DetailURI";

    private final int MY_LOADER_ID = 1;

    public static final String[] detailColumn = {
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING
    };

    static final int COL_WEATHER_ID = 0;
    static final int COL_WEATHER_DATE= 1;
    static final int COL_WEATHER_DESC = 2;
    static final int COL_WEATHER_MAX_TEMP = 3;
    static final int COL_WEATHER_MIN_TEMP = 4;
    static final int COL_WEATHER_HUMIDITY = 5;
    static final int COL_WEATHER_WIND_SPEED = 6;
    static final int COL_WEATHER_WIND_DEGREES = 7;
    static final int COL_WEATHER_PRESSURE = 8;
    static final int COL_WEATHER_CONDITION_ID = 9;
    static final int COL_WEATHER_LOCATION = 10;

    private ImageView mIconView;
    private TextView mDayView;
    private TextView mDateView;
    private TextView mHighView;
    private TextView mLowView;
    private TextView mDescView;
    private TextView mHumidityView;
    private TextView mWindView;
    private TextView mPressureView;
    private MyView mCompassView;


    public DetailFragment() {
        setHasOptionsMenu(true);
    }

//    public static DetailFragment newInstance(Uri uri){
//        DetailFragment ff = new DetailFragment();
//
//        Bundle args = new Bundle();
//        args.putParcelable(DETAIL_URI, uri);
//        ff.setArguments(args);
//
//        return ff;
//
//    }
////
//    public Uri getUri(){
//        Uri uri;
//        try {
//            uri = getArguments().getParcelable(DETAIL_URI);
//
//        } catch (NullPointerException e){
//            return null;
//
//        }
//        return uri;
//    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        if (null!=mUri) {
            return new CursorLoader(getActivity(), mUri, detailColumn, null, null, null);
        }
        return null;
    }

    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor){
        boolean isMetric = Utility.isMetric(getActivity());
        if (!cursor.moveToFirst())
            return;

        String weatherDate = Utility.formatDate(cursor.getLong(COL_WEATHER_DATE));

        int weatherId = cursor.getInt(COL_WEATHER_CONDITION_ID);

        mCompassView.setDirection(cursor.getFloat(COL_WEATHER_WIND_DEGREES));

        mIconView.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));

        String dayDate = Utility.getDayName(getActivity(), cursor.getLong(COL_WEATHER_DATE));
        mDayView.setText(dayDate);

        String fullDate = Utility.getFormattedMonthDay(getActivity(), cursor.getLong(COL_WEATHER_DATE));
        mDateView.setText(fullDate);

        String weatherDesc = cursor.getString(COL_WEATHER_DESC);
        mDescView.setText(weatherDesc);

        String highTemp =  Utility.formatTemperature(getActivity(), cursor.getDouble(COL_WEATHER_MAX_TEMP));
        mHighView.setText(highTemp);

        String lowTemp = Utility.formatTemperature(getActivity(), cursor.getDouble(COL_WEATHER_MIN_TEMP));
        mLowView.setText(lowTemp);

        String humidity = getActivity().getString(R.string.format_humidity, cursor.getFloat(COL_WEATHER_HUMIDITY));
        mHumidityView.setText(humidity);

        String windSpeed = Utility.getFormattedWind(getActivity(), cursor.getFloat(COL_WEATHER_WIND_SPEED), cursor.getFloat(COL_WEATHER_WIND_DEGREES));
        mWindView.setText(windSpeed);

        String pressure = getActivity().getString(R.string.format_pressure, cursor.getFloat(COL_WEATHER_PRESSURE));
        mPressureView.setText(pressure);

        mIconView.setContentDescription(weatherDesc);


        mDailyForecast = String.format("%s - %s - %s/%s",weatherDate, weatherDesc, highTemp,lowTemp);


        if (mShareActionProvider!=null){
            mShareActionProvider.setShareIntent(createShareIntent());
        }

    }

    public void onLoaderReset(Loader<Cursor> cursorLoader){        }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(MY_LOADER_ID, null, this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments!=null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
//            mUri = getUri();
        }
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mCompassView = (MyView) rootView.findViewById(R.id.list_item_compass);
        mIconView = (ImageView) rootView.findViewById(R.id.list_item_icon_imageview);
        mDayView = (TextView) rootView.findViewById(R.id.list_item_day_textview);
        mDateView = (TextView) rootView.findViewById(R.id.list_item_date_textview);
        mDescView = (TextView) rootView.findViewById(R.id.list_item_forecast_textview);
        mHighView = (TextView) rootView.findViewById(R.id.list_item_high_textview);
        mLowView = (TextView) rootView.findViewById(R.id.list_item_low_textview);
        mHumidityView = (TextView) rootView.findViewById(R.id.list_item_humidity_textview);
        mWindView = (TextView) rootView.findViewById(R.id.list_item_wind_textview);
        mPressureView = (TextView) rootView .findViewById(R.id.list_item_pressure_textview);
        return rootView;
    }

    private Intent createShareIntent(){
        Intent shareIntent = new Intent (Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mDailyForecast + SUNSHINE_HASHTAG);

        return shareIntent;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detailfragment, menu);

        MenuItem item = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        if (mDailyForecast!= null){
            mShareActionProvider.setShareIntent(createShareIntent());
        }
        else Log.d(LOG_TAG, "String not found");
    }

    void onLocationChanged( String newLocation ) {
        // replace the uri, since the location has changed
        Uri uri = mUri;
        if (null != uri) {
            long date = WeatherContract.WeatherEntry.getDateFromUri(uri);
            Uri updatedUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(newLocation, date);
            mUri = updatedUri;
            getLoaderManager().restartLoader(MY_LOADER_ID, null, this);
        }
    }

}
