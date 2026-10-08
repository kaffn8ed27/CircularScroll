package com.example.circularscroll

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class StatesDataTest {
    val context: Context = ApplicationProvider.getApplicationContext()
    val statesList = context.resources.getStringArray(R.array.us_states)

    @Test
    fun `states array contains 51 items`() {
        assertEquals(51, statesList.size)
    }

    @Test
    fun `states array contains DC`() {
        assert(statesList.contains("Washington, D.C."))
    }

    @Test
    fun `states array contains no duplicates`() {
        // Set will not allow duplicate entries
        assertEquals(statesList.toSet().size, statesList.size)
    }

    @Test
    fun `states array contains no null or blank entries` () {
        assertFalse(statesList.any {it.isNullOrBlank()})
    }
}
