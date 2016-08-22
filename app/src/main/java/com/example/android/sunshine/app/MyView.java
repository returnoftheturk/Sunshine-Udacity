package com.example.android.sunshine.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

/**
 * Created by Owner on 8/21/2016.
 */
public class MyView extends View {


    public MyView(Context context){
        super(context);
        init();
    }

    public MyView(Context context, AttributeSet attrs){
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyView, 0,0);
        try{
            mDirection = a.getFloat(R.styleable.MyView_direction, 0);
        } finally {
            a.recycle();
        }

        init();
    }

    public MyView(Context context, AttributeSet attrs, int defaultStyle){
        super(context, attrs, defaultStyle);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyView, 0,0);
        try{
            mDirection = a.getFloat(R.styleable.MyView_direction, 0);
        } finally {
            a.recycle();
        }

        init();
    }

    public void setDirection(float degrees){
        mDirection = degrees;

        AccessibilityManager accessibilityManager = (AccessibilityManager) getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (accessibilityManager.isEnabled()){

            sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
        }

        invalidate();
        requestLayout();
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
//        return super.dispatchPopulateAccessibilityEvent(event);
        event.getText().add(getDirectionDesc(mDirection));
        return true;
    }

    private String getDirectionDesc(float degrees){
        String windDirectionDesc = "Unknown";
        if (degrees >= 337.5 || degrees < 22.5) {
            windDirectionDesc = "North";
        } else if (degrees >= 22.5 && degrees < 67.5) {
            windDirectionDesc = "North East";
        } else if (degrees >= 67.5 && degrees < 112.5) {
            windDirectionDesc = "East";
        } else if (degrees >= 112.5 && degrees < 157.5) {
            windDirectionDesc = "South East";
        } else if (degrees >= 157.5 && degrees < 202.5) {
            windDirectionDesc = "South";
        } else if (degrees >= 202.5 && degrees < 247.5) {
            windDirectionDesc = "South West";
        } else if (degrees >= 247.5 && degrees < 292.5) {
            windDirectionDesc = "West";
        } else if (degrees >= 292.5 && degrees < 337.5) {
            windDirectionDesc = "North West";
        }

        return windDirectionDesc;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int hSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int wSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        int myHight = 400;
        int myWidth = 400;

        if (hSpecMode==MeasureSpec.EXACTLY){
            myHight = hSpecSize;
        } else if (hSpecMode==MeasureSpec.AT_MOST){
            myHight = Math.min(myHight, hSpecSize);
        } else if (hSpecMode==MeasureSpec.UNSPECIFIED){

        }

        if (wSpecMode==MeasureSpec.EXACTLY){
            myWidth = wSpecSize;
        } else if (wSpecMode==MeasureSpec.AT_MOST){
            myWidth = Math.min(myWidth, wSpecSize);
        } else if (wSpecMode==MeasureSpec.UNSPECIFIED){

        }
        setMeasuredDimension(myWidth, myHight);
    }

    Paint myPaint;
    Paint myArrowPaint;
    RectF myRectF;
    float mDirection;
    String directionDesc;

    public void init(){
        myPaint = new Paint();
        myPaint.setColor(Color.BLUE);
        myPaint.setStyle(Paint.Style.STROKE);

        myArrowPaint = new Paint();
        myArrowPaint.setColor(Color.BLACK);
        myArrowPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        myRectF = new RectF(50, 50, 350, 350 );

    }


    @Override
    protected void onDraw(Canvas canvas) {


        canvas.drawCircle(200,200,175, myPaint);

        canvas.drawCircle(200,200,150, myPaint);

        canvas.drawArc(myRectF, 0, 90, true, myPaint);
        canvas.drawArc(myRectF, 180, 90, true, myPaint);

        canvas.drawText("North", 175, 45, myPaint);
        canvas.drawText("South", 175, 365, myPaint);
        canvas.drawText("East", 355, 205, myPaint);
        canvas.drawText("West", 15, 205, myPaint);
        canvas.drawArc(myRectF, mDirection-90, 3, true, myArrowPaint);

    }
}
