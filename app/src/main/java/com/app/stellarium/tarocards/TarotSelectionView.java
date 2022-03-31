package com.app.stellarium.tarocards;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.app.stellarium.R;
import com.app.stellarium.tarocards.custom.BezierEvaluator;
import com.app.stellarium.utils.ScreenUtil;
import com.app.stellarium.utils.SlideScrollView;

import java.util.ArrayList;
import java.util.List;

public class TarotSelectionView extends FrameLayout {
    private static class CardXLocation {
        public int startX = 0;
        public int endX = 0;
    }

    Context context;
    FrameLayout cardContainer;
    SlideScrollView slideScrollView;
    ArrayList<ImageView> imageViews = new ArrayList<>();
    ArrayList<Boolean> isOpenImageView = new ArrayList<>();
    ImageView mIvTranslate;

    private static final int MAX_CARD_COUNT = 78;
    int screenHalfWidth = 0;
    List<CardXLocation> mCardLocations = new ArrayList<>();

    public TarotSelectionView(Context context, int countOfCards) {
        super(context, null);
        initView(context, countOfCards);
    }

    public TarotSelectionView(Context context, AttributeSet attrs, int countOfCards) {
        super(context, attrs, 0);
        initView(context, countOfCards);
    }

    public TarotSelectionView(Context context, AttributeSet attrs, int defStyleAttr, int countOfCards) {
        super(context, attrs, defStyleAttr);
        initView(context, countOfCards);
    }

    private void initView(Context context, int countOfCards) {
        this.context = context;
        screenHalfWidth = ScreenUtil.getScreenWidth(this.context) / 2;
        View view;
        if (countOfCards == 3) {
            view = inflate(context, R.layout.layout_selection_three_view, this);
        } else if (countOfCards == 4) {
            view = inflate(context, R.layout.layout_selection_four_view, this);
        } else if (countOfCards == 7) {
            view = inflate(context, R.layout.layout_selection_seven_view, this);
        } else {
            view = inflate(context, R.layout.layout_selection_one_view, this);
        }

        slideScrollView = view.findViewById(R.id.scrollView);
        cardContainer = view.findViewById(R.id.container);
        mIvTranslate = view.findViewById(R.id.iv_translate);
        slideScrollView.setScrollChangedListener((scrollX, scrollY, oldX, oldY) ->
        {
            int cardPosition = screenHalfWidth + scrollX;
            for (int i = 0; i < mCardLocations.size(); i++) {
                CardXLocation loc = mCardLocations.get(i);
                if (cardPosition >= loc.startX && cardPosition < loc.endX) {
                    return;
                }
            }
        });

        int cardDisplayWidth = ScreenUtil.dip2px(this.context, 30);
        int rightPadding = screenHalfWidth - ScreenUtil.dip2px(this.context, 35);
        int leftPadding = ScreenUtil.getScreenWidth(this.context) / 2 - cardDisplayWidth / 2;
        cardContainer.setPadding(leftPadding, 0, rightPadding, 0);

        for (int i = 0; i < MAX_CARD_COUNT; i++) {
            final View itemView = inflate(context, R.layout.item_card, null);
            itemView.setTag(false);
            itemView.setOnClickListener(v -> {
                if ((boolean) itemView.getTag()) {
                    slideScrollView.setSlide(false);
                    int[] loc = new int[2];
                    itemView.getLocationOnScreen(loc);
                    mIvTranslate.setX(loc[0]);
                    mIvTranslate.setY(loc[1]);
                    itemView.setVisibility(View.INVISIBLE);
                    mIvTranslate.setAlpha(1.0f);
                    mIvTranslate.setVisibility(View.VISIBLE);

                    if (!isOpenImageView.get(0)) {
                        startCardTranslateAnim(mIvTranslate, imageViews.get(0));
                        isOpenImageView.set(0, true);
                    } else if ((imageViews.size() > 1) && (!isOpenImageView.get(1))) {
                        startCardTranslateAnim(mIvTranslate, imageViews.get(1));
                        isOpenImageView.set(1, true);
                    } else if ((imageViews.size() > 1) && (!isOpenImageView.get(2))) {
                        startCardTranslateAnim(mIvTranslate, imageViews.get(2));
                        isOpenImageView.set(2, true);
                    } else if ((imageViews.size() > 3) && (!isOpenImageView.get(3))) {
                        startCardTranslateAnim(mIvTranslate, imageViews.get(3));
                        isOpenImageView.set(3, true);
                    } else if ((imageViews.size() > 4) && (!isOpenImageView.get(4))) {
                        startCardTranslateAnim(mIvTranslate, imageViews.get(4));
                        isOpenImageView.set(4, true);
                    } else if ((imageViews.size() > 4) && (!isOpenImageView.get(5))) {
                        startCardTranslateAnim(mIvTranslate, imageViews.get(5));
                        isOpenImageView.set(5, true);
                    } else if ((imageViews.size() > 4) && (!isOpenImageView.get(6))) {
                        startCardTranslateAnim(mIvTranslate, imageViews.get(6));
                        isOpenImageView.set(6, true);
                    }
                    if (imageViews.size() == 3 && isOpenImageView.get(2)) {
                        cardContainer.animate().alpha(0.0f).setDuration(500);
                        cardContainer.setVisibility(GONE);
                    }
                    if (imageViews.size() == 1 && isOpenImageView.get(0)) {
                        cardContainer.animate().alpha(0.0f).setDuration(500);
                        cardContainer.setVisibility(GONE);
                    }
                    if (imageViews.size() == 4 && isOpenImageView.get(3)) {
                        cardContainer.animate().alpha(0.0f).setDuration(500);
                        cardContainer.setVisibility(GONE);
                    }
                    if (imageViews.size() == 7 && isOpenImageView.get(6)) {
                        cardContainer.animate().alpha(0.0f).setDuration(500);
                        cardContainer.setVisibility(GONE);
                    }
                    return;
                }
                itemView.setTag(true);
                itemView.setTranslationY(ScreenUtil.dip2px(TarotSelectionView.this.context, 20));
            });
            LayoutParams p = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            p.leftMargin = i * cardDisplayWidth;

            itemView.setLayoutParams(p);
            cardContainer.addView(itemView);
            CardXLocation location = new CardXLocation();
            location.startX = leftPadding + p.leftMargin;
            location.endX = leftPadding + p.leftMargin + cardDisplayWidth;
            mCardLocations.add(location);
        }

    }

    public void showTarotSelectionView() {
        setVisibility(VISIBLE);
    }

    private void startCardTranslateAnim(final View startView, final View endView) {
        ValueAnimator animator = ValueAnimator.ofObject(new BezierEvaluator(),
                new PointF(startView.getX(), startView.getY()),
                new PointF(endView.getX(), endView.getY()));
        animator.addUpdateListener(animation -> {
            PointF pointF = (PointF) animation.getAnimatedValue();
            startView.setX(pointF.x);
            startView.setY(pointF.y);
        });
        animator.setDuration(1000);
        animator.addListener(new CardAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                slideScrollView.setSlide(true);
                cardDanceAnim(startView, endView);
            }
        });
        animator.start();
    }

    private void cardDanceAnim(View originView, final View targetView) {
        targetView.setVisibility(View.VISIBLE);
        AnimatorSet mDismissSet = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.tarot_rotate_dismiss);
        AnimatorSet mDisplaySet = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.tarot_rotate_display);
        mDismissSet.setTarget(originView);
        mDisplaySet.setTarget(targetView);
        mDisplaySet.addListener(new CardAnimatorListener() {
        });
        mDismissSet.start();
        mDisplaySet.start();
    }

    public void setPictures(ArrayList<ImageView> pictures) {
        imageViews.addAll(pictures);
        for (int i = 0; i < imageViews.size(); i++) {
            isOpenImageView.add(false);
        }
    }
}