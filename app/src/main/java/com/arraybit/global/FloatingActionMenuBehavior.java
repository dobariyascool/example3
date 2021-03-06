package com.arraybit.global;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.AttributeSet;
import android.view.View;

//import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

@SuppressWarnings("unchecked")
public class FloatingActionMenuBehavior extends CoordinatorLayout.Behavior {
    private float mTranslationY;

    public FloatingActionMenuBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        // && dependency instanceof Snackbar.SnackbarLayout
        if(child instanceof android.support.design.widget.FloatingActionButton && dependency instanceof Snackbar.SnackbarLayout){
            this.updateTranslation(parent, child, dependency);
        }
        else if (child instanceof FloatingActionButton && dependency instanceof Snackbar.SnackbarLayout) {
            this.updateTranslation(parent, child, dependency);
        }
        else if (child instanceof FloatingActionMenu && dependency instanceof Snackbar.SnackbarLayout) {
            this.updateTranslation(parent, child, dependency);
        }

        return false;
    }

    private void updateTranslation(CoordinatorLayout parent, View child, View dependency) {
        float translationY = this.getTranslationY(parent, child);
        if (translationY != this.mTranslationY) {
            ViewCompat.animate(child).cancel();
            if (Math.abs(translationY - this.mTranslationY) == (float) dependency.getHeight()) {
                ViewCompat.animate(child)
                        .translationY(translationY)
                        .setListener((ViewPropertyAnimatorListener) null);
            } else {
                ViewCompat.setTranslationY(child,translationY);
            }

            this.mTranslationY = translationY;
        }

    }

    private float getTranslationY(CoordinatorLayout parent, View child) {
        float minOffset = 0.0F;
        List dependencies = parent.getDependencies(child);
        int i = 0;

        for (int z = dependencies.size(); i < z; ++i) {
            View view = (View) dependencies.get(i);
            if (view instanceof Snackbar.SnackbarLayout && parent.doViewsOverlap(child, view)) {
                minOffset = Math.min(minOffset, ViewCompat.getTranslationY(view) - (float) view.getHeight());
            }
        }

        return minOffset;
    }

    /**
     * onStartNestedScroll and onNestedScroll will hide/show the FabMenu when a scroll is detected.
     */
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child,
                                       View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target,
                        nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed,
                dyUnconsumed);

        if (child instanceof FloatingActionButton) {
            FloatingActionButton fab = (FloatingActionButton) child;
            if (dyConsumed > 0 && fab.getVisibility() == View.VISIBLE) {
                fab.setVisibility(View.GONE);
            } else if (dyConsumed < 0 && fab.getVisibility() == View.GONE) {
                fab.setVisibility(View.VISIBLE);
            }
        }else if(child instanceof android.support.design.widget.FloatingActionButton){
            android.support.design.widget.FloatingActionButton  fab = (android.support.design.widget.FloatingActionButton) child;
            if (dyConsumed > 0 && fab.isShown()) {
                fab.hide();
            } else if (dyConsumed < 0 && !fab.isShown()) {
                fab.show();
            }
        }
        else if(child instanceof FloatingActionMenu){
            FloatingActionMenu fabMenu = (FloatingActionMenu) child;
            if (dyConsumed > 0 && !fabMenu.isMenuButtonHidden()) {
                fabMenu.hideMenuButton(true);
            } else if (dyConsumed < 0 && fabMenu.isMenuButtonHidden()) {
                fabMenu.showMenuButton(true);
            }
        }

    }
}