package com.example.android.sunshine.app;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
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
    private final int VIEW_TYPE_TODAY = 0;
    private final int VIEW_TYPE_FUTURE = 1;
    private static boolean mUseToday = true;

    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public void setmUseToday(boolean useToday){

        mUseToday = useToday;
    }

    @Override
    public int getItemViewType(int position) {
        return (position==0&&mUseToday) ? VIEW_TYPE_TODAY:VIEW_TYPE_FUTURE;
    }

    /*
                Remember that these views are reused as needed.
             */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;

        if (viewType==VIEW_TYPE_TODAY) {
            layoutId = R.layout.list_item_forecast_today;
        }else if (viewType==VIEW_TYPE_FUTURE) {
            layoutId = R.layout.list_item_forecast;
        }
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    public static class ViewHolder{
        public final ImageView iconView;
        public final TextView dateView;
        public final TextView highView;
        public final TextView lowView;
        public final TextView descView;

        public ViewHolder(View view){
            iconView = (ImageView) view.findViewById(R.id.list_item_icon_imageview);
            dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
            highView = (TextView) view.findViewById(R.id.list_item_high_textview);
            lowView = (TextView) view.findViewById(R.id.list_item_low_textview);
            descView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
        }




    }
    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder)view.getTag();

        int weatherId = cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID);

        int viewType = getItemViewType(cursor.getPosition());
        switch (viewType){
            case (VIEW_TYPE_TODAY):{
                viewHolder.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));
                break;
            }
            case (VIEW_TYPE_FUTURE):{
                viewHolder.iconView.setImageResource(Utility.getIconResourceForWeatherCondition(weatherId));
                break;
            }
        }

        long dateInMillis = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        String friendlyDayString = Utility.getFriendlyDayString(context, dateInMillis);
        viewHolder.dateView.setText(friendlyDayString);

        String highTemp = Utility.formatTemperature(context, cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),
                Utility.isMetric(context));
        viewHolder.highView.setText(highTemp);

        String lowTemp = Utility.formatTemperature(context, cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP),
                Utility.isMetric(context));
        viewHolder.lowView.setText(lowTemp);

        String weatherDesc = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        viewHolder.descView.setText(weatherDesc);
    }
}