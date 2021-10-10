package com.technzone.minibursa.ui.base.views.listeners

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerViewPaginationScrollListener(
    private val linearLayoutManager: LinearLayoutManager,
    private val pageSize: Int = DEFAULT_PAGE_SIZE
) : RecyclerView.OnScrollListener() {

    companion object {
        private const val DEFAULT_PAGE_SIZE = 20
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount: Int = linearLayoutManager.childCount
        val totalItemCount: Int = linearLayoutManager.itemCount
        val firstVisibleItemPosition: Int = linearLayoutManager.findFirstVisibleItemPosition()
        if (!isLoading() && !isLastPage()) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount &&
                firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE
            ) {
                loadMoreItems()
            }
        }
    }

    /**
     * This Called When Should Load Next Page
     */
    protected abstract fun loadMoreItems()

    /**
     * This Method Used To Set if should finished call loadMoreItems()
     */
    abstract fun isLastPage(): Boolean

    /**
     * This Method Checked If we getting data of not
     * if true will not notify to load more items
     */
    abstract fun isLoading(): Boolean
}