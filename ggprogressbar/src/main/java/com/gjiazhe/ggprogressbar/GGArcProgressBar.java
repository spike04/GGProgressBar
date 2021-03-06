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
        mIfRoundcorner = a.getBoolean(R.styleable.GGProgressBar_gpb_round_corner, false);

        a.recycle();
        setupPaints();
    }

    @Override
    protected void setupPaints() {
        super.setupPaints();
        mReachedBarPaint.setStyle(Paint.Style.STROKE);
        mReachedBarPaint.setStrokeWidth(mReachedBarWidth);
        if (mIfRoundcorner) {
            mReachedBarPaint.setStrokeCap(Paint.Cap.ROUND);
        }

        if (mIfDrawUnreachedBar) {
            mUnreachedBarPaint.setStyle(Paint.Style.STROKE);
            mUnreachedBarPaint.setStrokeWidth(mUnreachedBarWidth);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int diameter = Math.min(width, height);
        if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY) {
            width = diameter;
        }
        if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY) {
            height = diameter;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mIfDrawText){
            setupRectFWithText();
            canvas.drawText(mText, mDrawTextX, mDrawTextY, mTextPaint);
        }else {
            setupRectFWithoutText();
        }

        if (mIfDrawUnreachedBar) {
            canvas.drawArc(mUnreachedRectF, 0, 360, false, mUnreachedBarPaint);
        }
        canvas.drawArc(mReachedRectF, mStartAngle, mShownProgress * 360 / mMaxProgress, false, mReachedBarPaint);
    }

    protected void setupRectFWithoutText() {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int widthWithoutPadding = getWidth() - paddingLeft - paddingRight;
        int heightWithoutPadding = getHeight() - paddingTop - paddingBottom;
        int diameter = Math.min(widthWithoutPadding, heightWithoutPadding);

        float offset;
        if (mIfDrawUnreachedBar) {
            offset = (mReachedBarWidth > mUnreachedBarWidth) ? (mReachedBarWidth/2.0f) : (mUnreachedBarWidth/2.0f);
            mUnreachedRectF.left = paddingLeft + offset;
            mUnreachedRectF.top = paddingTop + offset;
            mUnreachedRectF.right = paddingLeft + diameter - offset;
            mUnreachedRectF.bottom = paddingTop + diameter - offset;
        } else {
            offset = mReachedBarWidth/2.0f;
        }

        mReachedRectF.left = paddingLeft + offset;
        mReachedRectF.top = paddingTop + offset;
        mReachedRectF.right = paddingLeft + diameter - offset;
        mReachedRectF.bottom = paddingTop + diameter - offset;
    }

    protected void setupRectFWithText() {
        setupRectFWithoutText();

        mText = String.format("%d", mShownProgress * 100 / mMaxProgress);
        mText = mPrefix + mText + mSuffix;

        float DrawTextWidth = mTextPaint.measureText(mText);
        mDrawTextX = (mReachedRectF.right + mReachedRectF.left)/2.0f - DrawTextWidth/2.0f;
        mDrawTextY = (mReachedRectF.bottom + mReachedRectF.top)/2.0f- (mTextPaint.descent() + mTextPaint.ascent())/2.0f;
    }

    public void setReachedBarWidth(float width) {
        mReachedBarWidth = width;
        mReachedBarPaint.setStrokeWidth(width);
    }

    public void setUnreachedBarWidth(float width) {
        if (mIfDrawUnreachedBar) {
            mUnreachedBarWidth = width;
            mUnreachedBarPaint.setStrokeWidth(width);
        }
    }

    public float getReachedBarWidth() {
        return mReachedBarWidth;
    }

    public float getUnreachedBarWidth() {
        return mUnreachedBarWidth;
    }

    public float getReachedBarRadius() {
        return (mReachedRectF.right - mReachedRectF.left + mReachedBarWidth)/2.0f;
    }

    public float getUnreachedBarRadius() {
        return (mUnreachedRectF.right - mUnreachedRectF.left + mUnreachedBarWidth)/2.0f;
    }

}
