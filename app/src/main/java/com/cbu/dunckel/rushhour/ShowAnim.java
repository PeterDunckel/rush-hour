package com.cbu.dunckel.rushhour;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Dunckel on 12/6/2016.
 */

public class ShowAnim extends Animation{
    int targetHeight;
    int initialHeight;
    View view;

    public ShowAnim(View view, int targetHeight, int initialHeight) {
        this.view = view;
        this.targetHeight = targetHeight;
        this.initialHeight = initialHeight;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        view.getLayoutParams().height = initialHeight + (int) ((targetHeight-initialHeight) * interpolatedTime);
        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth,
                           int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
