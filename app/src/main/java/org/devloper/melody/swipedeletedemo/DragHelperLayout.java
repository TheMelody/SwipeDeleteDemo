package org.devloper.melody.swipedeletedemo;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by fuqiang on 2016/9/7.
 */
public class DragHelperLayout extends LinearLayout {
    private static final String TAG="DragHelperLayout";
    private View mContentView;
    private View mCancleView;
    private ViewDragHelper mHelper;
    private int mDistance;
    private int draggedX;
    private final double AUTO_OPEN_SPEED_LIMIT = 400.0;

    public DragHelperLayout(Context context) {
        super(context);
        initDragHelper();
    }

    public DragHelperLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDragHelper();
    }

    public DragHelperLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDragHelper();
    }

    private void initDragHelper() {
        mHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == mContentView || child == mCancleView;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (child == mContentView) {
                    final int leftBound = getPaddingLeft();
                    final int minLeftBound = -leftBound - mDistance;
                    final int newLeft = Math.min(Math.max(minLeftBound, left), 0);
                    return newLeft;
                } else {
                    final int minLeftBound = getPaddingLeft()
                            + mContentView.getMeasuredWidth() - mDistance;
                    final int maxLeftBound = getPaddingLeft()
                            + mContentView.getMeasuredWidth() + getPaddingRight();
                    final int newLeft = Math.min(Math.max(left, minLeftBound),
                            maxLeftBound);
                    return newLeft;
                }
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return mDistance;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                draggedX = left;
                getParent().requestDisallowInterceptTouchEvent(true);
                if (changedView == mContentView) {
                    mCancleView.offsetLeftAndRight(dx);
                } else {
                    mContentView.offsetLeftAndRight(dx);
                }
                invalidate();
                Log.e(TAG, ">>>left:" + left + ",top;" + top + ",dx:" + dx + ",dy:" + dy);
            }

            // 手指释放的时候回调
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                boolean settleToOpen = false;
                if (xvel > AUTO_OPEN_SPEED_LIMIT) {
                    settleToOpen = false;
                } else if (xvel < -AUTO_OPEN_SPEED_LIMIT) {
                    settleToOpen = true;
                } else if (draggedX <= -mDistance / 2) {
                    settleToOpen = true;
                } else if (draggedX > -mDistance / 2) {
                    settleToOpen = false;
                }
                final int settleDestX = settleToOpen ? -mDistance : 0;
                if (releasedChild == mContentView) {
                    if (settleDestX == 0) {
                        mHelper.settleCapturedViewAt(0, 0);
                    } else {
                        mHelper.settleCapturedViewAt(settleDestX, 0);
                    }
                }
                invalidate();
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mHelper.processTouchEvent(ev);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mDistance = mCancleView.getMeasuredWidth();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = getChildAt(0);
        mCancleView = getChildAt(1);
    }
}