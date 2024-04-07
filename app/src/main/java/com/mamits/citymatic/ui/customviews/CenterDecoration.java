package com.mamits.citymatic.ui.customviews;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Px;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CenterDecoration extends RecyclerView.ItemDecoration {
    private int spacing;
    private int firstViewWidth = -1;
    private int lastViewWidth = -1;

    public CenterDecoration(@Px int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int adapterPosition = parent.getChildAdapterPosition(view);
        LinearLayoutManager lm = (LinearLayoutManager) parent.getLayoutManager();
        if (adapterPosition == 0) {
            // Invalidate decorations when this view width has changed
            if (view.getWidth() != firstViewWidth) {
                view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        parent.invalidateItemDecorations();
                        view.getViewTreeObserver().removeOnPreDrawListener(this);
                        return true;
                    }
                });
            }
            firstViewWidth = view.getWidth();
            outRect.left = parent.getWidth() / 2 - view.getWidth() / 2;
            // If we have more items, use the spacing provided
            if (lm.getItemCount() > 1) {
                outRect.right = spacing / 2;
            } else {
                // Otherwise, make sure this to fill the whole width with the decoration
                outRect.right = outRect.left;
            }
        } else if (adapterPosition == lm.getItemCount() - 1) {
            // Invalidate decorations when this view width has changed
            if (view.getWidth() != lastViewWidth) {
                view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        parent.invalidateItemDecorations();
                        view.getViewTreeObserver().removeOnPreDrawListener(this);
                        return true;
                    }
                });
            }
            lastViewWidth = view.getWidth();
            outRect.right = parent.getWidth() / 2 - view.getWidth() / 2;
            outRect.left = spacing / 2;
        } else {
            outRect.left = spacing / 2;
            outRect.right = spacing / 2;
        }
    }
}

