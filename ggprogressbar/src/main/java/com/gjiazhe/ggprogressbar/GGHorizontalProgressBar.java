package com.gjiazhe.ggprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * Created by gjz on 12/11/15.
 */
public class GGHorizontalProgressBar extends GGProgressBar {

    /**
     * The height of the reached area.
     */
    private float mReachedBarHeight;

    /**
     * The height of the unreached area.
     */
    private float mUnreachedBarHeight;

    private boolean mIfRoundcorner;

    public GGHorizontalProgressBar(Context context) {
        this(context, null);
    }

    public GGHorizontalProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GGHorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final float default_reached_bar_height = dp2px(1.5f);
        final float default_unreached_bar_height = dp2px(1.0f);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GGProgressBar, defStyleAttr, 0);

        mReachedBarHeight = a.getDimension(R.styleable.GGProgressBar_gpb_reached_bar_height, default_reached_bar_height);
        mUnreachedBarHeight = a.getDimension(R.styleable.GGProgressBar_gpb_unreached_bar_height, default_unreached_bar_height);
        mIfRoundcorner = a.getBoolean(R.styleable.GGProgressBar_gpb_round_corner, false);

        a.recycle();
        setupPaints();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height;
        int height_mode = MeasureSpec.getMode(heightMeasureSpec);
        if (height_mode == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            height = getMinHeight() + getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);
    }

    private int getMinHeight() {
        int result = Math.max((int) mReachedBarHeight, (int) mUnreachedBarHeight);
        if (mIfDrawText) {
            result = Math.max(result, (int)(mTextSize));
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mIfDrawText){
            setupRectFWithText();
            canvas.drawText(mDrawText, mDrawTextX, mDrawTextY, mTextPaint);
        }else {
            setupRectFWithoutText();
        }

        if (mIfDrawReachedBar) {
            drawBar(canvas, mReachedRectF, mReachedBarHeight, mReachedBarPaint);
        }
        if (mIfDrawUnreachedBar && mUnreachedRectF.left < getWidth() - getPaddingRight()) {
            drawBar(canvas, mUnreachedRectF, mUnreachedBarHeight, mUnreachedBarPaint);
        }
    }

    private void drawBar(Canvas canvas, RectF bar, float barSize, Paint barPaint) {
        if (mIfRoundcorner) {
            float cornerRadius = barSize/2.0f;
            canvas.drawRoundRect(bar, cornerRadius, cornerRadius, barPaint);
        } else {
            canvas.drawRect(bar, barPaint);
        }
    }


    @Override
    protected void setupRectFWithoutText() {
        int heightWithoutPadding = getHeight() - getPaddingTop() - getPaddingBottom();

        mReachedRectF.left = getPaddingLeft();
        mReachedRectF.top = getPaddingTop() + heightWithoutPadding/2.0f - mReachedBarHeight/2.0f;
        mReachedRectF.right = mReachedRectF.left + (getWidth() - getPaddingLeft() - getPaddingRight()) / (mMaxProgress * 1.0f) * mShownProgress;
        mReachedRectF.bottom = mReachedRectF.top + mReachedBarHeight;

        if (mIfDrawUnreachedBar) {
            mUnreachedRectF.left = mReachedRectF.right;
            mUnreachedRectF.right = getWidth() - getPaddingRight();
            mUnreachedRectF.top = getPaddingTop() + heightWithoutPadding/2.0f - mUnreachedBarHeight/2.0f;
            mUnreachedRectF.bottom = mUnreachedRectF.top + mUnreachedBarHeight;
        }
    }

    @Override
    protected void setupRectFWithText() {
        mDrawText = String.format("%d", mShownProgress * 100 / mMaxProgress);
        mDrawText = mPrefix + mDrawText + mSuffix;

        float DrawTextWidth = mTextPaint.measureText(mDrawText);
        mDrawTextY = getPaddingTop() + (getHeight() - getPaddingTop() - getPaddingBottom())/2.0f - (mTextPaint.descent() + mTextPaint.ascent())/2.0f;

        if (getShownProgress() == 0) {
            mIfDrawReachedBar = false;
            mDrawTextX = getPaddingLeft();
        } else {
            mIfDrawReachedBar = true;
            mReachedRectF.left = getPaddingLeft();
            mReachedRectF.top = getPaddingTop() + (getHeight() - getPaddingTop() - getPaddingBottom())/2.0f - mReachedBarHeight/2.0f;
            mReachedRectF.right = (getWidth() - getPaddingLeft() - getPaddingRight()) / (getMax() * 1.0f) * getShownProgress() - mTextOffset + getPaddingLeft();
            mReachedRectF.bottom = mReachedRectF.top + mReachedBarHeight;
            mDrawTextX = (mReachedRectF.right + mTextOffset);
        }

        if ((mDrawTextX + DrawTextWidth) >= getWidth() - getPaddingRight()) {
            mDrawTextX = getWidth() - getPaddingRight() - DrawTextWidth;
            mReachedRectF.right = mDrawTextX - mTextOffset;
        }

        if (mIfDrawUnreachedBar) {
            mUnreachedRectF.left = mDrawTextX + DrawTextWidth + mTextOffset;
            mUnreachedRectF.top = getPaddingTop() + (getHeight() - getPaddingTop() - getPaddingBottom())/2.0f - mUnreachedBarHeight/2.0f;
            mUnreachedRectF.right = getWidth() - getPaddingRight();
            mUnreachedRectF.bottom = mUnreachedRectF.top + mUnreachedBarHeight;
        }
    }

    public void setReachedBarHeight(float height) {
        mReachedBarHeight = height;
    }

    public void setUnreachedBarHeight(float height) {
        mUnreachedBarHeight = height;
    }

    public float getReachedBarHeight() {
        return mReachedBarHeight;
    }

    public float getUnreachedBarHeight() {
        return mUnreachedBarHeight;
    }
}
