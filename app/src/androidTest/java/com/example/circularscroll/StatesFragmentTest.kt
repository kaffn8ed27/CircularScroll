package com.example.circularscroll

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI tests for the states list hosted in [StatesFragment].
 */
@RunWith(AndroidJUnit4::class)
class StatesFragmentTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun `States list is displayed`() {
        onView(withId(R.id.states_list)).check(matches(isDisplayed()))
    }

    @Test
    fun `States list scrolls horizontally`() {
        activityRule.scenario.onActivity { activity ->
            val list = activity.findViewById<RecyclerView>(R.id.states_list)
            val layoutManager = list.layoutManager as LinearLayoutManager
            assertEquals(RecyclerView.HORIZONTAL, layoutManager.orientation)
        }
    }

    @Test
    fun `States list contains 153 items`() {
        activityRule.scenario.onActivity { activity ->
            val list = activity.findViewById<RecyclerView>(R.id.states_list)
            assertEquals(153, list.adapter?.itemCount)
        }
    }

    @Test
    fun `First state name is Alabama`() {
        onView(withText("Alabama")).check(matches(isDisplayed()))
    }

    @Test
    fun `Scrolling to end shows Wyoming`() {
        onView(withId(R.id.states_list))
            .perform(RecyclerViewActions.scrollToLastPosition<StateAdapter.ViewHolder>())
        onView(withText("Wyoming")).check(matches(isDisplayed()))
    }

    @Test
    fun `Scrolling to Virginia displays it`() {
        // There will be 3 copies - we'll match the middle one, since that is the "real" copy of the data
        onView(withId(R.id.states_list))
            .perform(
                RecyclerViewActions.scrollTo<StateAdapter.ViewHolder>(withText("Virginia"))
                    .atPosition(1)
            )
        onView(withText("Virginia")).check(matches(isDisplayed()))
    }

    @Test
    fun `Scrolling before Alabama wraps around to Wyoming`() {
        var realCount = 0
        activityRule.scenario.onActivity { activity ->
            val list = activity.findViewById<RecyclerView>(R.id.states_list)
            realCount = (list.adapter as StateAdapter).realCount
        }

        // The item just before the middle copy is the last state of the first copy
        onView(withId(R.id.states_list))
            .perform(RecyclerViewActions.scrollToPosition<StateAdapter.ViewHolder>(realCount - 1))
        onView(withText("Wyoming")).check(matches(isDisplayed()))

        // The silent scroll should have moved the list back into the middle copy
        activityRule.scenario.onActivity { activity ->
            val list = activity.findViewById<RecyclerView>(R.id.states_list)
            val first = (list.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            assertTrue(first in realCount until realCount * 2)
        }
    }

    @Test
    fun `Scrolling past Wyoming wraps around to Alabama`() {
        var realCount = 0
        activityRule.scenario.onActivity { activity ->
            val list = activity.findViewById<RecyclerView>(R.id.states_list)
            realCount = (list.adapter as StateAdapter).realCount

            // The item just after the middle copy is the first state of the last copy. Pin it
            // to the start edge; a plain scrollToPosition would align it to the end edge, leaving
            // the first visible item in the middle copy so no wrap would happen.
            (list.layoutManager as LinearLayoutManager)
                .scrollToPositionWithOffset(realCount * 2, 0)
        }
        onView(withText("Alabama")).check(matches(isDisplayed()))

        // The silent scroll should have moved the list back into the middle copy
        activityRule.scenario.onActivity { activity ->
            val list = activity.findViewById<RecyclerView>(R.id.states_list)
            val first = (list.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            assertTrue(first in realCount until realCount * 2)
        }
    }
}
