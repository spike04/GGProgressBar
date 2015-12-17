package com.gjiazhe.ggprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
    private final int LETF_RIGHT = 1;
    private final int TOP_BOTTOM = 2;
    private final int RIGHT_LEFT = 3;
    private final int BOTTOM_TOP = 4;
    private boolean isHorizontal;

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

    private int heightWithoutPadding;
    private int widthWithoutPadding;

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

        mOrientation = a.getInt(R.styleable.GGProgressBar_orientation, LETF_RIGHT);
        if (mOrientation == LETF_RIGHT || mOrientation == RIGHT_LEFT) {
            isHorizontal = true;
            mReachedBarHeight = a.getDimension(R.styleable.GGProgressBar_gpb_reached_bar_height, default_reached_bar_size);
            mUnreachedBarHeight = a.getDimension(R.styleable.GGProgressBar_gpb_unreached_bar_height, default_unreached_bar_size);
        } else {
            isHorizontal = false;
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
        if (isHorizontal) {
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
        heightWithoutPadding = getHeight() - getPaddingTop() - getPaddingBottom();
        widthWithoutPadding = getWidth() - getPaddingLeft() - getPaddingRight();

        if (mIfDrawText){
            setupRectFWithText();
            canvas.drawText(mText, mDrawTextX, mDrawTextY, mTextPaint);
        }else {
            setupRectFWithoutText(canvas);
        }

        if (isHorizontal) {
            if (mIfDrawReachedBar) {
                drawBar(canvas, mReachedRectF, mReachedBarHeight, mReachedBarPaint);
            }
            if (mIfDrawUnreachedBar) {
                drawBar(canvas, mUnreachedRectF, mUnreachedBarHeight, mUnreachedBarPaint);
            }
        } else {
            if (mIfDrawReachedBar) {
                drawBar(canvas, mReachedRectF, mReachedBarWidth, mReachedBarPaint);
            }
            if (mIfDrawUnreachedBar) {
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

    protected void setupRectFWithText() {
        switch (mOrientation) {
            case LETF_RIGHT:setupRectFWithText_leftToRight();break;
            case TOP_BOTTOM:setupRectFWithText_topToBottom();break;
            case RIGHT_LEFT:setupRectFWithText_rightToLeft();break;
            case BOTTOM_TOP:setupRectFWithText_bottomToTop();break;
        }
    }
    private void setupRectFWithText_leftToRight() {
        formatText();
        float drawTextWidth = mTextPaint.measureText(mText);
        mDrawTextY = getPaddingTop() + (heightWithoutPadding)/2.0f - (mTextPaint.descent() + mTextPaint.ascent())/2.0f;

        if (mShownProgress == 0) {
            mIfDrawReachedBar = false;
            mDrawTextX = getPaddingLeft();
        } else {
            mIfDrawReachedBar = true;
            mReachedRectF.left = getPaddingLeft();
            mReachedRectF.top = getPaddingTop() + (heightWithoutPadding)/2.0f - mReachedBarHeight/2.0f;
            mReachedRectF.right = mReachedRectF.left + widthWithoutPadding / (getMax() * 1.0f) * getShownProgress() - mTextOffset - drawTextWidth/2.0f;
            mReachedRectF.bottom = mReachedRectF.top + mReachedBarHeight;
            mDrawTextX = mReachedRectF.right + mTextOffset;
        }

        if ((mDrawTextX + drawTextWidth) >= getWidth() - getPaddingRight()) {
            mDrawTextX = getWidth() - getPaddingRight() - drawTextWidth;
            mReachedRectF.right = mDrawTextX - mTextOffset;
        }

        if (mIfDrawUnreachedBar) {
            mUnreachedRectF.left = mDrawTextX + drawTextWidth + mTextOffset;
            mUnreachedRectF.top = getPaddingTop() + (heightWithoutPadding)/2.0f - mUnreachedBarHeight/2.0f;
            mUnreachedRectF.right = getWidth() - getPaddingRight();
            mUnreachedRectF.bottom = mUnreachedRectF.top + mUnreachedBarHeight;
        }
    }

    private void setupRectFWithText_rightToLeft() {
        formatText();
        float drawTextWidth = mTextPaint.measureText(mText);
        mDrawTextY = getPaddingTop() + (heightWithoutPadding)/2.0f - (mTextPaint.descent() + mTextPaint.ascent())/2.0f;

        if (mShownProgress == 0) {
            mIfDrawReachedBar = false;
            mDrawTextX = getWidth() - getPaddingRight() - drawTextWidth;
        } else {
            mIfDrawReachedBar = true;
            mReachedRectF.right =getWidth() - getPaddingRight();
            mReachedRectF.top = getPaddingTop() + (heightWithoutPadding)/2.0f - mReachedBarHeight/2.0f;
            mReachedRectF.left = mReachedRectF.right - widthWithoutPadding / (getMax() * 1.0f) * getShownProgress() + mTextOffset + drawTextWidth/2.0f;
            mReachedRectF.bottom = mReachedRectF.top + mReachedBarHeight;
            mDrawTextX = mReachedRectF.left - mTextOffset - drawTextWidth;
        }

        if (mDrawTextX < getPaddingLeft()) {
            mDrawTextX = getPaddingLeft();
            mReachedRectF.left = mDrawTextX + drawTextWidth + mTextOffset;
        }

        if (mIfDrawUnreachedBar) {
            mUnreachedRectF.right = mDrawTextX - mTextOffset;
            mUnreachedRectF.top = getPaddingTop() + (heightWithoutPadding)/2.0f - mUnreachedBarHeight/2.0f;
            mUnreachedRectF.left = getPaddingLeft();
            mUnreachedRectF.bottom = mUnreachedRectF.top + mUnreachedBarHeight;
        }
    }

    private void setupRectFWithText_topToBottom() {
        formatText();
        float drawTextWidth = mTextPaint.measureText(mText);
        mDrawTextX = getPaddingLeft() + widthWithoutPadding /2.0f - drawTextWidth/2.0f;

        if (mShownProgress == 0) {
            mIfDrawReachedBar = false;
            mDrawTextY = getPaddingTop() + mTextSize - mTextPaint.descent();
        } else {
            mIfDrawReachedBar = true;
            mDrawTextY = getPaddingTop() + heightWithoutPadding / (mMaxProgress * 1.0f) * mShownProgress - (mTextPaint.ascent() + mTextPaint.descent())/2.0f;
            if (mDrawTextY + mTextPaint.descent() > getHeight() - getPaddingBottom()) {
                mDrawTextY = getHeight() - getPaddingBottom() - mTextPaint.descent();
            } else if (mDrawTextY + mTextPaint.ascent() < getPaddingTop()){
                mDrawTextY = getPaddingTop() + mTextSize - mTextPaint.descent();
            }
            mReachedRectF.left = getPaddingLeft() + widthWithoutPadding /2.0f - mReachedBarWidth/2.0f;
            mReachedRectF.top = getPaddingTop();
            mReachedRectF.right = mReachedRectF.left + mReachedBarWidth;
            mReachedRectF.bottom = mDrawTextY + mTextPaint.ascent() - mTextOffset;
        }

        if (mIfDrawUnreachedBar) {
            mUnreachedRectF.left = getPaddingLeft() + widthWithoutPadding/2.0f - mUnreachedBarWidth/2.0f;
            mUnreachedRectF.top = mDrawTextY + mTextPaint.descent() + mTextOffset;
            mUnreachedRectF.right = mUnreachedRectF.left + mUnreachedBarWidth;
            mUnreachedRectF.bottom = getHeight() - getPaddingBottom();
        }
    }

    private void setupRectFWithText_bottomToTop() {
        formatText();
        float drawTextWidth = mTextPaint.measureText(mText);
        mDrawTextX = getPaddingLeft() + widthWithoutPadding /2.0f - drawTextWidth/2.0f;

        if (mShownProgress == 0) {
            mIfDrawReachedBar = false;
            mDrawTextY = getHeight() - getPaddingBottom() - mTextPaint.descent();
        } else {
            mIfDrawReachedBar = true;
            mDrawTextY = getHeight() - getPaddingBottom() - heightWithoutPadding / (mMaxProgress * 1.0f) * mShownProgress - (mTextPaint.ascent() + mTextPaint.descent())/2.0f;
            if (mDrawTextY + mTextPaint.descent() > getHeight() - getPaddingBottom()) {
                mDrawTextY = getHeight() - getPaddingBottom() - mTextPaint.descent();
            } else if (mDrawTextY + mTextPaint.ascent() < getPaddingTop()){
                mDrawTextY = getPaddingTop() + mTextSize - mTextPaint.descent();
            }
            mReachedRectF.left =  getPaddingLeft() + widthWithoutPadding /2.0f - mReachedBarWidth/2.0f;
            mReachedRectF.bottom = getHeight() - getPaddingBottom();
            mReachedRectF.right = mReachedRectF.left + mReachedBarWidth;
            mReachedRectF.top = mDrawTextY + mTextPaint.descent() + mTextOffset;
        }

        if (mIfDrawUnreachedBar) {
            mUnreachedRectF.left = getPaddingLeft() + widthWithoutPadding/2.0f - mUnreachedBarWidth/2.0f;
            mUnreachedRectF.bottom = mDrawTextY + mTextPaint.ascent() - mTextOffset;
            mUnreachedRectF.right = mUnreachedRectF.left + mUnreachedBarWidth;
            mUnreachedRectF.top = getPaddingTop();
        }
    }

    protected void setupRectFWithoutText(Canvas canvas) {
        switch (mOrientation) {
            case LETF_RIGHT:setupRectWithoutText_leftToRight();break;
            case TOP_BOTTOM:setupRectWithoutText_topToBottom();break;
            case RIGHT_LEFT:canvas.rotate(180, getWidth()/2.0f, getHeight()/2.0f);setupRectWithoutText_leftToRight();break;
            case BOTTOM_TOP:canvas.rotate(180, getWidth()/2.0f, getHeight()/2.0f);setupRectWithoutText_topToBottom();break;
        }
    }

    private void setupRectWithoutText_leftToRight() {
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

    private void setupRectWithoutText_topToBottom() {
        mReachedRectF.left = getPaddingLeft() + widthWithoutPadding/2.0f - mReachedBarWidth/2.0f;
        mReachedRectF.top = getPaddingTop();
        mReachedRectF.right = mReachedRectF.left + mReachedBarWidth;
        mReachedRectF.bottom = mReachedRectF.top +  heightWithoutPadding / (mMaxProgress * 1.0f) * mShownProgress;

        if (mIfDrawUnreachedBar) {
            mUnreachedRectF.left = getPaddingLeft() + widthWithoutPadding/2.0f - mUnreachedBarWidth/2.0f;
            mUnreachedRectF.top = mReachedRectF.bottom;
            mUnreachedRectF.right = mUnreachedRectF.left + mUnreachedBarWidth;
            mUnreachedRectF.bottom = getHeight() - getPaddingBottom();
        }
    }

    private void formatText() {
        mText = String.format("%d", mShownProgress * 100 / mMaxProgress);
        mText = mPrefix + mText + mSuffix;
    }

    public float getReachedBarHeight() {
        if (isHorizontal)
            return mReachedBarHeight;
        else
            return 0;
    }

    public float getUnreachedBarHeight() {
        if (isHorizontal)
            return mUnreachedBarHeight;
        else
            return 0;
    }

    public float getReachedBarWidth() {
        if (!isHorizontal)
            return mReachedBarWidth;
        else
            return 0;
    }

    public float getUnreachedBarWidth() {
        if (!isHorizontal)
            return mUnreachedBarWidth;
        else
            return 0;
    }

    public void setReachedBarHeight(float height) {
        if (isHorizontal)
            mReachedBarHeight = height;
    }

    public void setUnreachedBarHeight(float height) {
        if (isHorizontal)
            mUnreachedBarHeight = height;
    }

    public void setReachedBarWidth(float width) {
        if (!isHorizontal)
            mReachedBarWidth = width;
    }

    public void setUnreachedBarWidth(float width) {
        if (!isHorizontal)
            mUnreachedBarWidth = width;
    }
}
