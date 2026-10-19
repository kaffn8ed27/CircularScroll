package com.example.circularscroll

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView

class StatesFragment : Fragment(R.layout.fragment_states) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val states = resources.getStringArray(R.array.us_states).sorted()
        val adapter = StateAdapter(states)
        val list = view.findViewById<RecyclerView>(R.id.states_list)
        val layoutManager = list.layoutManager as LinearLayoutManager
        list.adapter = adapter

        // Start at the beginning of the middle copy. On recreation (e.g. rotation) skip this,
        // since a pending scroll position would override the RecyclerView's restored state.
        if (savedInstanceState == null) {
            layoutManager.scrollToPosition(adapter.realCount)
        }

        list.addOnScrollListener(CircularScrollListener(layoutManager, adapter.realCount))
    }
}

/**
 * Returns how many positions to jump so that [firstVisible] lands back in the middle copy
 * of the data: [realCount] if it's in the first copy, -[realCount] if it's in the last copy,
 * or 0 if it's already in the middle copy.
 */
internal fun wrapShift(firstVisible: Int, realCount: Int) = when {
    firstVisible < realCount -> realCount // drifted into the first copy
    firstVisible >= realCount * 2 -> -realCount // drifted into the last copy
    else -> 0 // still in middle copy, no need to move
}

/**
 * Keeps the first visible item inside the middle copy by silently jumping one copy's
 * length whenever it drifts into the first or last copy. Because every copy is identical,
 * the jump preserves the on-screen pixel offset and is invisible to the user.
 *
 * Assumes the viewport is narrower than one copy, so the last copy is always available
 * as a buffer to the right of the middle copy.
 */
private class CircularScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val realCount: Int,
) : RecyclerView.OnScrollListener() {

    private val orientationHelper =
        OrientationHelper.createOrientationHelper(layoutManager, layoutManager.orientation)

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        // Scroll events triggered by a layout (e.g. scrollToPosition) arrive mid-layout, where
        // a new layout request is dropped, so defer the jump until the layout pass finishes
        if (recyclerView.isInLayout) {
            recyclerView.post { wrapIfNeeded() }
        } else {
            wrapIfNeeded()
        }
    }

    private fun wrapIfNeeded() {
        val first = layoutManager.findFirstVisibleItemPosition()
        if (first == RecyclerView.NO_POSITION) return

        val shift = wrapShift(first, realCount)
        if (shift == 0) return

        val firstView = layoutManager.findViewByPosition(first) ?: return
        val offset = orientationHelper.getDecoratedStart(firstView) -
            orientationHelper.startAfterPadding
        layoutManager.scrollToPositionWithOffset(first + shift, offset)
    }
}
