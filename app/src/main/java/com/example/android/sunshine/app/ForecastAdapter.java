package com.example.android.sunshine.app;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sunshine.app.data.WeatherContract;

import org.w3c.dom.Text;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {
    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_forecast, parent, false);

        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int weatherId = cursor.getInt(ForecastFragment.COL_WEATHER_ID);

        ImageView imageView = (ImageView) view.findViewById(R.id.list_item_icon);
        imageView.setImageResource(R.drawable.ic_launcher);

        long dateInMillis = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        String friendlyDayString = Utility.getFriendlyDayString(context, dateInMillis);
        TextView dayStringTv = (TextView)view.findViewById(R.id.list_item_date_textview);
        dayStringTv.setText(friendlyDayString);

        String highTemp = Utility.formatTemperature(cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),
                Utility.isMetric(context));
        TextView highTempTv = (TextView)view.findViewById(R.id.list_item_high_textview);
        highTempTv.setText(highTemp);

        String lowTemp = Utility.formatTemperature(cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP),
                Utility.isMetric(context));
        TextView lowTempTv = (TextView)view.findViewById(R.id.list_item_low_textview);
        lowTempTv.setText(lowTemp);

        String weatherDesc = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        TextView weatherDescTv = (TextView)view.findViewById(R.id.list_item_forecast_textview);
        weatherDescTv.setText(weatherDesc);
    }
}