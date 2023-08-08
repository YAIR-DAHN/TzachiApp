package com.shahareinisim.tzachiapp.Utils;

import android.animation.Animator;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
//import android.view.animation.AccelerateInterpolator;
//import android.view.animation.DecelerateInterpolator;

public class Animations {

    final static long DURATION = 300;
    public static void show(View topBar) {
        topBar.animate()
                .setDuration(DURATION)
                .translationY(0)
//                .setInterpolator(new DecelerateInterpolator(2))
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        topBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
    }


    public static void hide(View actionBar) {
        actionBar.animate()
                .setDuration(DURATION)
                .translationY(convertToPX(-100))
//                .setInterpolator(new AccelerateInterpolator(2))
                .setListener(endListener(() -> actionBar.setVisibility(View.GONE))).start();
    }

    public static Animator.AnimatorListener endListener(Runnable onAnimationEnd) {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                onAnimationEnd.run();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
    }

    public static int convertToPX(int dp) {
        Resources r = Resources.getSystem();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, r.getDisplayMetrics());
    }
}

