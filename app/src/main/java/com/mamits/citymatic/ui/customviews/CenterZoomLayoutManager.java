package com.mamits.citymatic.ui.customviews;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CenterZoomLayoutManager extends LinearLayoutManager {

    private final float mShrinkAmount = 0.15f;
    private final float mShrinkDistance = 0.9f;


    public CenterZoomLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getOrientation() == HORIZONTAL) {
            int scrolled = super.scrollHorizontallyBy(dx, recycler, state);
            scaleChildren();
            return scrolled;
        } else {
            return 0;
        }

    }

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        scaleChildren();
    }

    private void scaleChildren() {
        float midpoint = getWidth() / 2f;
        float d1 = mShrinkDistance * midpoint;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            float d = Math.min(d1, Math.abs(midpoint - (getDecoratedRight(child) + getDecoratedLeft(child)) / 2f));
            float scale = 1f - mShrinkAmount * d / d1;
            child.setScaleX(scale);
            child.setScaleY(scale);
        }
    }
}
