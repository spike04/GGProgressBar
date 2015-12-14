package com.gjiazhe.ggprogressbar;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by gjz on 12/14/15.
 */
public class GGLinearProgressBar extends GGProgressBar {

    private final int ORIENTATION_VERTICAL = 1;
    private final int ORIENTATION_HORIZONTAL = 2;

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

    public GGLinearProgressBar(Context context) {
        this(context, null);
    }

    public GGLinearProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GGLinearProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void setupRectFWithText() {

    }

    @Override
    protected void setupRectFWithoutText() {

    }
}
