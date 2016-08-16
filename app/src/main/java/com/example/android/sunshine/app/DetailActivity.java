/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.sunshine.app.data.WeatherContract;

public class DetailActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
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
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
        private final String LOG_TAG = DetailFragment.class.getSimpleName();
        public final String SUNSHINE_HASHTAG = " #SunshineApp";
        public String mDailyForecast;
        private final int MY_LOADER_ID = 1;

        public static final String[] detailColumn = {
                WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
                WeatherContract.WeatherEntry.COLUMN_DATE,
                WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
                WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
                WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
        };

        static final int COL_WEATHER_ID = 0;
        static final int COL_WEATHER_DATE= 1;
        static final int COL_WEATHER_DESC = 2;
        static final int COL_WEATHER_MAX_TEMP = 3;
        static final int COL_WEATHER_MIN_TEMP = 4;

        private ShareActionProvider mShareActionProvider;

        public DetailFragment() {
            setHasOptionsMenu(true);
        }

        public Loader<Cursor> onCreateLoader(int id, Bundle args){
            Intent intent = getActivity().getIntent();
            if (intent ==null)
                return null;

            return new CursorLoader(getActivity(), intent.getData(), detailColumn, null, null, null);
        }

        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor){
            boolean isMetric = Utility.isMetric(getActivity());
            if (!cursor.moveToFirst())
                return;


            String weatherDate = Utility.formatDate(cursor.getLong(COL_WEATHER_DATE));
            String weatherDesc = cursor.getString(COL_WEATHER_DESC);
            String highTemp =  Utility.formatTemperature(cursor.getDouble(COL_WEATHER_MAX_TEMP), isMetric);
            String lowTemp = Utility.formatTemperature(cursor.getDouble(COL_WEATHER_MIN_TEMP), isMetric);
            mDailyForecast = weatherDate + "-" + weatherDesc + "-" + highTemp + "/" + lowTemp;
            TextView textView = (TextView) getView().findViewById(R.id.detail_text);
            textView.setText(mDailyForecast);

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

            return inflater.inflate(R.layout.fragment_detail, container, false);
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

    }
}