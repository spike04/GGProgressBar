package com.gjiazhe.ggprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * Created by gjz on 12/14/15.
 */
public class GGLinearProgressBar extends GGProgressBar {

    /**
     * flag of orizentation
     */
    private int mOrientation;
    private final int ORIENTATION_HORIZONTAL = 1;
    private final int ORIENTATION_VERTICAL = 2;

    /**
     * The height of the reached area, used when the bar is horizontal
     */
    private float mReachedBarHeight;

    /**
     * The height of the unreached area, used when the bar is horizontal
     */
    private float mUnreachedBarHeight;

    /**
     * The width of the reached area, used when the bar is vertical
     */
    private float mReachedBarWidth;

    /**
     * The width of the reached area, used when the bar is vertical
     */
    private float mUnreachedBarWidth;

    /**
     * if the reached bar and unreached bar has round corners
     */
    private boolean mIfRoundcorner;

    public GGLinearProgressBar(Context context) {
        this(context, null);
    }

    public GGLinearProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GGLinearProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final float default_reached_bar_size = dp2px(1.5f);
        final float default_unreached_bar_size = dp2px(1.0f);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GGProgressBar, defStyleAttr, 0);

        mOrientation = a.getInt(R.styleable.GGProgressBar_orientation, ORIENTATION_HORIZONTAL);
        if (mOrientation == ORIENTATION_HORIZONTAL) {
            mReachedBarHeight = a.getDimension(R.styleable.GGProgressBar_gpb_reached_bar_height, default_reached_bar_size);
            mUnreachedBarHeight = a.getDimension(R.styleable.GGProgressBar_gpb_unreached_bar_height, default_unreached_bar_size);
        } else {
            mReachedBarWidth = a.getDimension(R.styleable.GGProgressBar_gpb_reached_bar_width, default_reached_bar_size);
            mUnreachedBarWidth = a.getDimension(R.styleable.GGProgressBar_gpb_unreached_bar_width, default_unreached_bar_size);
        }
        mIfRoundcorner = a.getBoolean(R.styleable.GGProgressBar_gpb_round_corner, false);

        a.recycle();
        setupPaints();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        if (mOrientation == ORIENTATION_HORIZONTAL) {
            width = MeasureSpec.getSize(widthMeasureSpec);
            int height_mode = MeasureSpec.getMode(heightMeasureSpec);
            if (height_mode == MeasureSpec.EXACTLY) {
                height = MeasureSpec.getSize(heightMeasureSpec);
            } else {
                height = getMinHeightForHorizontalBar();
            }
        } else {
            height = MeasureSpec.getSize(heightMeasureSpec);
            int width_mode = MeasureSpec.getMode(widthMeasureSpec);
            if (width_mode == MeasureSpec.EXACTLY) {
                width = MeasureSpec.getSize(widthMeasureSpec);
            } else {
                width = getMinWidthForVerticalBar();
            }
        }

        setMeasuredDimension(width, height);
    }

    private int getMinHeightForHorizontalBar() {
        int result = Math.max((int) mReachedBarHeight, (int) mUnreachedBarHeight);
        if (mIfDrawText) {
            result = Math.max(result, (int) (mTextSize));
        }
        return result + getPaddingTop() + getPaddingBottom();
    }

    private int getMinWidthForVerticalBar() {
        int result = Math.max((int)mReachedBarWidth, (int)mUnreachedBarWidth);
        if (mIfDrawText) {
            result = Math.max(result, (int)(mTextPaint.measureText(mPrefix + "100" + mSuffix)));
        }
        return result + getPaddingLeft() + getPaddingRight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mIfDrawText){
            setupRectFWithText();
            canvas.drawText(mDrawText, mDrawTextX, mDrawTextY, mTextPaint);
        }else {
            setupRectFWithoutText();
        }

        if (mOrientation == ORIENTATION_HORIZONTAL) {
            if (mIfDrawReachedBar) {
                drawBar(canvas, mReachedRectF, mReachedBarHeight, mReachedBarPaint);
            }
            if (mIfDrawUnreachedBar && mUnreachedRectF.left < getWidth() - getPaddingRight()) {
                drawBar(canvas, mUnreachedRectF, mUnreachedBarHeight, mUnreachedBarPaint);
            }
        } else {
            if (mIfDrawReachedBar) {
                drawBar(canvas, mReachedRectF, mReachedBarWidth, mReachedBarPaint);
            }
            if (mIfDrawUnreachedBar && mUnreachedRectF.top < getHeight() - getPaddingBottom()) {
                drawBar(canvas, mUnreachedRectF, mUnreachedBarWidth, mUnreachedBarPaint);
            }
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
    protected void setupRectFWithText() {
        if (mOrientation == ORIENTATION_HORIZONTAL) {
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
        } else {
            mDrawText = String.format("%3d", mShownProgress * 100 / mMaxProgress);
            mDrawText = mPrefix + mDrawText + mSuffix;

            int widthWithoutpadding = getWidth() - getPaddingLeft() - getPaddingRight();
            float drawTextWidth = mTextPaint.measureText(mPrefix + "100" + mSuffix);
            mDrawTextX = getPaddingLeft() + widthWithoutpadding/2.0f - drawTextWidth/2.0f;

            if (mShownProgress == 0) {
                mIfDrawReachedBar = false;
                mDrawTextY = getPaddingTop() + mTextSize/2.0f - (mTextPaint.descent() + mTextPaint.ascent())/2.0f;
            } else {
                mIfDrawReachedBar = true;
                mReachedRectF.left = getPaddingLeft() + widthWithoutpadding/2.0f - mReachedBarWidth/2.0f;
                mReachedRectF.top = getPaddingTop();
                mReachedRectF.right = mReachedRectF.left + mReachedBarWidth;
                mReachedRectF.bottom = getPaddingTop() +  (getHeight() - getPaddingTop() - getPaddingBottom()) / (mMaxProgress * 1.0f) * mShownProgress - mTextOffset;
                mDrawTextY = mReachedRectF.bottom + mTextOffset + mTextSize/2.0f - (mTextPaint.descent() + mTextPaint.ascent())/2.0f;
            }

            if (mDrawTextY + mTextPaint.descent() > getHeight() - getPaddingBottom()) {
                mDrawTextY = getHeight() - getPaddingBottom() - mTextPaint.descent();
                mReachedRectF.bottom = mDrawTextY - mTextOffset - mTextSize/2.0f + (mTextPaint.descent() + mTextPaint.ascent())/2.0f;
            }

            if (mIfDrawUnreachedBar) {
                mUnreachedRectF.left = getPaddingLeft() + widthWithoutpadding/2.0f - mUnreachedBarWidth/2.0f;
                mUnreachedRectF.top = mDrawTextY + mTextPaint.descent() + mTextOffset;
                mUnreachedRectF.right = mUnreachedRectF.left + mUnreachedBarWidth;
                mUnreachedRectF.bottom = getHeight() - getPaddingBottom();
            }
        }
    }
    @Override
    protected void setupRectFWithoutText() {
        if (mOrientation == ORIENTATION_HORIZONTAL) {
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
        } else {
            int widthWithoutPadding = getWidth() - getPaddingLeft() - getPaddingRight();

            mReachedRectF.left = getPaddingLeft() + widthWithoutPadding/2.0f - mReachedBarWidth/2.0f;
            mReachedRectF.top = getPaddingTop();
            mReachedRectF.right = mReachedRectF.left + mReachedBarWidth;
            mReachedRectF.bottom = mReachedRectF.top +  (getHeight() - getPaddingTop() - getPaddingBottom()) / (mMaxProgress * 1.0f) * mShownProgress;

            if (mIfDrawUnreachedBar) {
                mUnreachedRectF.left = getPaddingLeft() + widthWithoutPadding/2.0f - mUnreachedBarWidth/2.0f;
                mUnreachedRectF.top = mReachedRectF.bottom;
                mUnreachedRectF.right = mUnreachedRectF.left + mUnreachedBarWidth;
                mUnreachedRectF.bottom = getHeight() - getPaddingBottom();
            }
        }
    }

    public float getReachedBarHeight() {
        if (mOrientation == ORIENTATION_HORIZONTAL)
            return mReachedBarHeight;
        else
            return 0;
    }

    public float getUnreachedBarHeight() {
        if (mOrientation == ORIENTATION_HORIZONTAL)
            return mUnreachedBarHeight;
        else
            return 0;
    }

    public float getReachedBarWidth() {
        if (mOrientation == ORIENTATION_VERTICAL)
            return mReachedBarWidth;
        else
            return 0;
    }

    public float getUnreachedBarWidth() {
        if (mOrientation == ORIENTATION_VERTICAL)
            return mUnreachedBarWidth;
        else
            return 0;
    }

    public void setReachedBarHeight(float height) {
        if (mOrientation == ORIENTATION_HORIZONTAL)
            mReachedBarHeight = height;
    }

    public void setUnreachedBarHeight(float height) {
        if (mOrientation == ORIENTATION_HORIZONTAL)
            mUnreachedBarHeight = height;
    }

    public void setReachedBarWidth(float width) {
        if (mOrientation == ORIENTATION_VERTICAL)
            mReachedBarWidth = width;
    }

    public void setUnreachedBarWidth(float width) {
        if (mOrientation == ORIENTATION_VERTICAL)
            mUnreachedBarWidth = width;
    }
}
