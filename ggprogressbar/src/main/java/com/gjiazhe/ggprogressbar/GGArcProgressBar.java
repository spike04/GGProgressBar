package com.gjiazhe.ggprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * Created by gjz on 12/11/15.
 */
public class GGArcProgressBar extends GGProgressBar {

    private float mReachedBarWidth;
    private float mUnreachedBarWidth;
    private float mStartAngle;

    public GGArcProgressBar(Context context) {
        this(context, null);
    }

    public GGArcProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GGArcProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final float default_reached_bar_width = dp2px(1.5f);
        final float default_unreached_bar_width = dp2px(1.0f);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GGProgressBar, defStyleAttr, 0);

        mReachedBarWidth = a.getDimension(R.styleable.GGProgressBar_gpb_reached_bar_width, default_reached_bar_width);
        mUnreachedBarWidth = a.getDimension(R.styleable.GGProgressBar_gpb_unreached_bar_width, default_unreached_bar_width);
        mStartAngle = a.getFloat(R.styleable.GGProgressBar_gpb_start_angle, 0);

        a.recycle();
        setupPaints();
    }

    @Override
    protected void setupPaints() {
        super.setupPaints();
        mReachedBarPaint.setStyle(Paint.Style.STROKE);
        mReachedBarPaint.setStrokeWidth(mReachedBarWidth);
        if (mIfDrawUnreachedBar) {
            mUnreachedBarPaint.setStyle(Paint.Style.STROKE);
            mUnreachedBarPaint.setStrokeWidth(mUnreachedBarWidth);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mIfDrawText){
            setupRectFWithText();
            canvas.drawText(mDrawText, mDrawTextX, mDrawTextY, mTextPaint);
        }else {
            setupRectFWithoutText();
        }

        if (mIfDrawUnreachedBar) {
            canvas.drawArc(mUnreachedRectF, 0, 360, false, mUnreachedBarPaint);
        }
        canvas.drawArc(mReachedRectF, mStartAngle, mShownProgress * 360 / mMaxProgress, false, mReachedBarPaint);
    }

    @Override
    protected void setupRectFWithoutText() {
        int padding = Math.max(Math.max(getPaddingLeft(), getPaddingRight()), Math.max(getPaddingTop(), getPaddingBottom()));
        int size = getWidth() < getHeight() ? getWidth() : getHeight();
        if (mIfDrawUnreachedBar) {
            mUnreachedRectF.left = mUnreachedRectF.top = padding + mUnreachedBarWidth/2;
            mUnreachedRectF.right = mUnreachedRectF.bottom = size - padding - mUnreachedBarWidth/2;
        }

        mReachedRectF.left = mReachedRectF.top = padding + mReachedBarWidth/2;
        mReachedRectF.right = mReachedRectF.bottom = size - padding - mReachedBarWidth/2;

    }

    @Override
    protected void setupRectFWithText() {
        setupRectFWithoutText();

        mDrawText = String.format("%d", mShownProgress * 100 / mMaxProgress);
        mDrawText = mPrefix + mDrawText + mSuffix;

        float DrawTextWidth = mTextPaint.measureText(mDrawText);
        mDrawTextX = (mReachedRectF.right + mReachedRectF.left)/2.0f - DrawTextWidth/2.0f;
        mDrawTextY = (mReachedRectF.bottom + mReachedRectF.top)/2.0f- (mTextPaint.descent() + mTextPaint.ascent())/2.0f;
    }

    public void setReachedBarWidth(float width) {
        mReachedBarWidth = width;
    }

    public void setUnreachedBarWidth(float width) {
        mUnreachedBarWidth = width;
    }

    public float getReachedBarWidth() {
        return mReachedBarWidth;
    }

    public float getUnreachedBarWidth() {
        return mUnreachedBarWidth;
    }

}
