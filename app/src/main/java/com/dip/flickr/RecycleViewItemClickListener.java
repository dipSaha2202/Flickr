package com.dip.flickr;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

class RecycleViewItemClickListener extends RecyclerView.SimpleOnItemTouchListener {
    private static final String TAG = "Result";
    boolean tap = false;

    interface OnRecycleViewItemClickListener{
        void onRecycleItemClick(View view, int position);
        void onRecycleItemLongClick(View view, int position);
    }

    private final OnRecycleViewItemClickListener itemClickListener;
    private final GestureDetectorCompat gestureDetector;

    public RecycleViewItemClickListener(Context context, final RecyclerView view, final OnRecycleViewItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        gestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (tap){
                Log.d(TAG, "onSingleTapUp: : called");
                View childView = view.findChildViewUnder(e.getX(), e.getY());
                if (childView != null && itemClickListener != null){
                    itemClickListener.onRecycleItemClick(childView, view.getChildAdapterPosition(childView));
                }
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View childView = view.findChildViewUnder(e.getX(), e.getY());
                if (childView != null && itemClickListener != null){
                    itemClickListener.onRecycleItemLongClick(childView, view.getChildAdapterPosition(childView));
                }
               // super.onLongPress(e);
            }
        });

    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        if (gestureDetector != null){
            tap = gestureDetector.onTouchEvent(e);
            Log.d(TAG, "onInterceptTouchEvent: return : " + tap);
            return gestureDetector.onTouchEvent(e);

        } else {
            return false;
        }
       // return super.onInterceptTouchEvent(rv, e);
    }
}
