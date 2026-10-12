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
    fun `States list contains 51 items`() {
        activityRule.scenario.onActivity { activity ->
            val list = activity.findViewById<RecyclerView>(R.id.states_list)
            assertEquals(51, list.adapter?.itemCount)
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
        onView(withId(R.id.states_list))
            .perform(RecyclerViewActions.scrollTo<StateAdapter.ViewHolder>(withText("Virginia")))
        onView(withText("Virginia")).check(matches(isDisplayed()))
    }
}
