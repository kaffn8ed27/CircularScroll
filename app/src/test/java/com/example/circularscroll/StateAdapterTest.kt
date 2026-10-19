package com.example.circularscroll

import android.content.Context
import android.view.ContextThemeWrapper
import android.widget.FrameLayout
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests that [StateAdapter] repeats its data across [StateAdapter.MULTIPLIER] copies,
 * using a small fake list so the expectations don't depend on the real states data.
 */
@RunWith(AndroidJUnit4::class)
class StateAdapterTest {
    // The item layout uses Material text attributes, so it needs the app theme to inflate
    private val context: Context = ContextThemeWrapper(
        ApplicationProvider.getApplicationContext(),
        R.style.Theme_CircularScroll,
    )
    private val states = listOf("Alpha", "Bravo", "Charlie")
    private val adapter = StateAdapter(states)

    private fun textAt(position: Int): CharSequence {
        val holder = adapter.createViewHolder(FrameLayout(context), 0)
        adapter.bindViewHolder(holder, position)
        return holder.textView.text
    }

    @Test
    fun `realCount is the size of one copy`() {
        assertEquals(3, adapter.realCount)
    }

    @Test
    fun `item count covers every copy`() {
        assertEquals(3 * StateAdapter.MULTIPLIER, adapter.itemCount)
    }

    @Test
    fun `start of each copy binds the first item`() {
        for (copy in 0 until StateAdapter.MULTIPLIER) {
            assertEquals("Alpha", textAt(copy * adapter.realCount))
        }
    }

    @Test
    fun `end of each copy binds the last item`() {
        for (copy in 0 until StateAdapter.MULTIPLIER) {
            assertEquals("Charlie", textAt((copy + 1) * adapter.realCount - 1))
        }
    }
}
