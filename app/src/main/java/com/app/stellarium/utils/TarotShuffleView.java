package com.app.stellarium.utils;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.app.stellarium.R;
import com.app.stellarium.utils.custom.AnimHelper;

public class TarotShuffleView extends FrameLayout {

    Context context;
    int translate = 20;
    int totalCardCount = 10;
    OnShuffleViewListener onShuffleViewListener;
    LayoutInflater inflater;
    private static final int CARD_DEFAULT_WIDTH = 100;
    private static final int CARD_ZOOM_IN_WIDTH = 91;
    private static final int CARD_ZOOM_IN_HEIGHT = 130;
    private static final int CARD_TOP_MARGIN = 150;
    private static final int CARD_TOP_UP = 50;

    public TarotShuffleView(Context context) {
        super(context, null);
        initView(context);
    }

    public TarotShuffleView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initView(context);
    }

    public TarotShuffleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setOnShuffleListener(OnShuffleViewListener listener) {
        this.onShuffleViewListener = listener;
    }

    private void initView(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(getContext());
        for (int i = 0; i < totalCardCount; i++) {
            View view = inflater.inflate(R.layout.item_shuffle_view, this, false);
            ImageView cardView = view.findViewById(R.id.card_view);
            int width = ScreenUtil.dip2px(this.context, CARD_ZOOM_IN_WIDTH);
            int height = ScreenUtil.dip2px(this.context, CARD_ZOOM_IN_HEIGHT);
            LayoutParams p = new LayoutParams(width, height);
            p.topMargin = ScreenUtil.dip2px(this.context, CARD_TOP_MARGIN);
            p.gravity = Gravity.CENTER_HORIZONTAL;
            cardView.setLayoutParams(p);
            cardView.setVisibility(View.GONE);

            if (i <= 4) {
                cardView.setTranslationX(translate * (4 - i));
                cardView.setTranslationY(translate * (4 - i));
                cardView.setVisibility(VISIBLE);
            }

            this.addView(view);
        }
    }

    public void anim() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            ImageView cardView = view.findViewById(R.id.card_view);
            cardView.setVisibility(VISIBLE);

            if (i <= 4) {
                AnimHelper.zoomOutAnimatorWithTranslation(cardView, translate * (4 - i), translate * (4 - i));
                continue;
            }

            if (i == getChildCount() - 1) {

                AnimHelper.zoomOutAnimator(cardView, new CardAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        startShuffleAnimator();
                    }
                });
                continue;
            }

            AnimHelper.zoomOutAnimator(cardView, listenerNull);
        }
    }

    private void startShuffleAnimator() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            int position = count - 1 - i;
            View view = getChildAt(position);
            View cardView = view.findViewById(R.id.card_view);
            if (position == 0) {
                AnimHelper.startAnimation(1000, 1350, cardView, 450, 200, 400, listenerNull);
            } else if (position == 1) {
                AnimHelper.startAnimation(1300, 1300, cardView, 600, 300, 600, new CardAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        cuttingCardAnimator(new CardAnimatorListener() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                translationYToTop();
                            }
                        });
                    }
                });
            } else if (position == 2) {
                AnimHelper.startAnimation(1250, 1050, cardView, 550, 260, 520, listenerNull);
            } else if (position == 3) {
                AnimHelper.startAnimation(1500, 900, cardView, 500, 250, 500, listenerNull);
            } else if (position == 4) {
                AnimHelper.startAnimation(1300, 750, cardView, 580, 280, 560, listenerNull);
            } else if (position == 5) {
                AnimHelper.startAnimation(1400, 600, cardView, 650, 340, 680, listenerNull);
            } else if (position == 6) {
                AnimHelper.startAnimation(1400, 450, cardView, 670, 320, 640, listenerNull);
            } else if (position == 7) {
                AnimHelper.startAnimation(1400, 300, cardView, 630, 320, 640, listenerNull);
            } else if (position == 8) {
                AnimHelper.startAnimation(1300, 150, cardView, 700, 350, 700, listenerNull);
            } else {
                AnimHelper.startAnimation(1500, 0, cardView, 720, 360, 720, listenerNull);
            }
        }
    }

    private void cuttingCardAnimator(final CardAnimatorListener listener) {
        final View view1 = getChildAt(0);
        final View view2 = getChildAt(1);
        final View view3 = getChildAt(2);
        final View view4 = getChildAt(3);
        final View view5 = getChildAt(4);
        final View view6 = getChildAt(5);
        final View view7 = getChildAt(6);
        final View view8 = getChildAt(7);
        final View view9 = getChildAt(8);
        final View view10 = getChildAt(9);
        AnimHelper.translateXAnim(view4, view1, listenerNull);
        AnimHelper.translateXAnim(view5, view2, listenerNull);
        AnimHelper.translateXAnim(view6, view3, new CardAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                AnimHelper.translateXAnim(view10, view9, listenerNull);
                AnimHelper.translateXAnim(view8, view7, listenerNull);
                AnimHelper.translateXAnim(view6, view5, listenerNull);
                AnimHelper.translateXAnim(view4, view3, listenerNull);
                AnimHelper.translateXAnim(view2, view1, listener);
            }
        });
    }

    private void translationYToTop() {
        int scaleY = CARD_ZOOM_IN_WIDTH - CARD_DEFAULT_WIDTH;
        int translateY = scaleY + CARD_TOP_UP;
        int toY = ScreenUtil.dip2px(context, translateY);

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            View cardView = view.findViewById(R.id.card_view);
            if (i == count - 1) {
                AnimHelper.translateYToTopAnim(cardView, CARD_TOP_UP, new CardAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (onShuffleViewListener == null) return;
                        onShuffleViewListener.onShuffleAnimDone();
                    }
                });
            } else {
                AnimHelper.translateYToTopAnim(cardView, toY, listenerNull);
            }
        }

    }

    CardAnimatorListener listenerNull = new CardAnimatorListener() {
    };


}
