package com.app.stellarium.transitionGenerator;

import android.graphics.RectF;
import android.view.animation.Interpolator;

import com.flaviofaria.kenburnsview.Transition;
import com.flaviofaria.kenburnsview.TransitionGenerator;

import java.util.Random;

public class StellariumTransitionGenerator implements TransitionGenerator {

    private final Random mRandom = new Random(System.currentTimeMillis());

    private long mTransitionDuration;

    private Interpolator mTransitionInterpolator;

    private Transition mLastGenTrans;

    private RectF mLastDrawableBounds;



    public StellariumTransitionGenerator(long transitionDuration, Interpolator transitionInterpolator) {
        setTransitionDuration(transitionDuration);
        setTransitionInterpolator(transitionInterpolator);
    }


    @Override
    public Transition generateNextTransition(RectF drawableBounds, RectF viewport) {
        boolean firstTransition = mLastGenTrans == null;
        boolean drawableBoundsChanged = true;
        boolean viewportRatioChanged = true;

        RectF srcRect = null;
        RectF dstRect = null;

        if (!firstTransition) {
            dstRect = mLastGenTrans.getDestinyRect();
            drawableBoundsChanged = !drawableBounds.equals(mLastDrawableBounds);
            viewportRatioChanged = !StellariumMathUtils.haveSameAspectRatio1(dstRect, viewport);
        }

        if (dstRect == null || drawableBoundsChanged || viewportRatioChanged) {
            srcRect = generateRandomRect(drawableBounds, viewport);
        } else {
            srcRect = dstRect;
        }
        dstRect = generateRandomRect(drawableBounds, viewport);

        mLastGenTrans = new Transition(srcRect, dstRect, mTransitionDuration,
                mTransitionInterpolator);

        mLastDrawableBounds = new RectF(drawableBounds);

        return mLastGenTrans;
    }

    private RectF generateRandomRect(RectF drawableBounds, RectF viewportRect) {
        float drawableRatio = StellariumMathUtils.getRectRatio1(drawableBounds);
        float viewportRectRatio = StellariumMathUtils.getRectRatio1(viewportRect);
        RectF maxCrop;

        if (drawableRatio > viewportRectRatio) {
            float r = (drawableBounds.height() / viewportRect.height()) * viewportRect.width();
            float b = drawableBounds.height();
            maxCrop = new RectF(0, 0, r, b);
        } else {
            float r = drawableBounds.width();
            float b = (drawableBounds.width() / viewportRect.width()) * viewportRect.height();
            maxCrop = new RectF(0, 0, r, b);
        }

        float factor = 0.9f;

        float width = factor * maxCrop.width();
        float height = factor * maxCrop.height();
        int widthDiff = (int) (drawableBounds.width() - width);
        int heightDiff = (int) (drawableBounds.height() - height);
        int left = widthDiff > 0 ? mRandom.nextInt(widthDiff) : 0;
        int top = heightDiff > 0 ? mRandom.nextInt(heightDiff) : 0;
        return new RectF(left, top, left + width, top + height);
    }

    public void setTransitionDuration(long transitionDuration) {
        mTransitionDuration = transitionDuration;
    }

    public void setTransitionInterpolator(Interpolator interpolator) {
        mTransitionInterpolator = interpolator;
    }
}
