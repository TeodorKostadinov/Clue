package com.inveitix.android.clue.cmn;

import android.util.Log;
import android.view.ScaleGestureDetector;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.inveitix.android.clue.ui.views.RoomView;

/**
 * Created by Tito on 4.10.2016 г..
 */

public class MyScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {
    private static final int MIN_WIDTH = 100;
    private int mW, mH;
    private RoomView mVodView;
    private FrameLayout.LayoutParams mRootParam;

    public MyScaleGestureListener(FrameLayout.LayoutParams mRootParam, RoomView mVodView) {
        this.mRootParam = mRootParam;
        this.mVodView = mVodView;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        mW *= detector.getScaleFactor();
        mH *= detector.getScaleFactor();
        if (mW < MIN_WIDTH) {
            mW = mVodView.getWidth();
            mH = mVodView.getHeight();
        }
        Log.d("onScale", "scale=" + detector.getScaleFactor() + ", w=" + mW + ", h=" + mH);
        mVodView.setFixedViewSize(mW, mH);
        mRootParam.width = mW;
        mRootParam.height = mH;
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        mW = mVodView.getWidth();
        mH = mVodView.getHeight();
        Log.d("onScaleBegin", "scale=" + detector.getScaleFactor() + ", w=" + mW + ", h=" + mH);
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        Log.d("onScaleEnd", "scale=" + detector.getScaleFactor() + ", w=" + mW + ", h=" + mH);
    }
}
