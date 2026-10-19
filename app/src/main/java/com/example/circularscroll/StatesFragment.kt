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

        // Start at the beginning of the middle copy
        layoutManager.scrollToPosition(adapter.realCount)

        // TEMP: disable fling while getting the basic wrap-around working
        list.onFlingListener = object : RecyclerView.OnFlingListener() {
            override fun onFling(velocityX: Int, velocityY: Int) = true
        }

        list.addOnScrollListener(CircularScrollListener(layoutManager, adapter.realCount))
    }
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
        val first = layoutManager.findFirstVisibleItemPosition()
        if (first == RecyclerView.NO_POSITION) return

        val shift = when {
            first < realCount -> realCount // drifted into the first copy
            first >= realCount * 2 -> -realCount // drifted into the last copy
            else -> return
        }

        val firstView = layoutManager.findViewByPosition(first) ?: return
        val offset = orientationHelper.getDecoratedStart(firstView) -
            orientationHelper.startAfterPadding
        layoutManager.scrollToPositionWithOffset(first + shift, offset)
    }
}
