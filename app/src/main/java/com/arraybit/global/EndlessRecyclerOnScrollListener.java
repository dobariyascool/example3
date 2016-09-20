package com.arraybit.global;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();
    public static boolean isFirstScreen = false;
    int firstVisibleItem, visibleItemCount, totalItemCount, lastVisibleItem;
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 2; // The minimum amount of items to have below your current scroll position before loading more.
    private int current_page = 1;
    private LinearLayoutManager mLinearLayoutManager;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private int startingPageIndex = 0;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    public EndlessRecyclerOnScrollListener(StaggeredGridLayoutManager staggeredGridLayoutManager) {
        this.staggeredGridLayoutManager = staggeredGridLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

//        if(staggeredGridLayoutManager!=null){
//            visibleItemCount = recyclerView.getChildCount();
//            totalItemCount = staggeredGridLayoutManager.getItemCount();
//            int mSpanCount = 2;
//            int[] into = new int[mSpanCount];
//            firstVisibleItem = staggeredGridLayoutManager.findFirstVisibleItemPositions(into)[0];
//        }
//        else{
        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
//        }

//        if (totalItemCount < previousTotal) {
//            this.current_page = this.startingPageIndex;
//            this.previousTotal = totalItemCount;
//            if (totalItemCount == 0) {
//                this.loading = true;
//            }
//        }
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount > previousTotal)) {
            loading = false;
            previousTotal = totalItemCount;
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            current_page++;
            onLoadMore(current_page);
            loading = true;
        }
//        if (loading) {
//            if (totalItemCount > previousTotal) {
//                loading = false;
//                previousTotal = totalItemCount;
//            }
//        }
//        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
//            // End has been reached
//
//            current_page++;
//
//            onLoadMore(current_page);
//
//            loading = true;
//        }
    }

    public abstract void onLoadMore(int current_page);
}