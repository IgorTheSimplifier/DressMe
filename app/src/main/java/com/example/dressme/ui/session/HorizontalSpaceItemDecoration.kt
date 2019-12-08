package com.example.dressme.ui.session

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView

class HorizontalSpaceItemDecoration: RecyclerView.ItemDecoration() {
    // externalize
    private val verticalRightSpacingHeight : Int = 40
    private val verticalLeftSpacingHeight : Int = 40

    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        outRect.right = verticalRightSpacingHeight
        outRect.left = verticalLeftSpacingHeight
    }
}