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

    @Test
    fun `states array contains 51 items`() {
        val states = context.resources.getStringArray(R.array.us_states)
        assertEquals(51, states.size)
    }
}