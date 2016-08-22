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
        invalidate();
        requestLayout();
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
