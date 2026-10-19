package com.example.circularscroll

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Boundary tests for [wrapShift], using 3 copies of a 10-item list:
 * first copy 0..9, middle copy 10..19, last copy 20..29.
 */
class WrapShiftTest {
    private val realCount = 10

    @Test
    fun `start of first copy jumps forward one copy`() {
        assertEquals(realCount, wrapShift(0, realCount))
    }

    @Test
    fun `end of first copy jumps forward one copy`() {
        assertEquals(realCount, wrapShift(realCount - 1, realCount))
    }

    @Test
    fun `start of middle copy does not jump`() {
        assertEquals(0, wrapShift(realCount, realCount))
    }

    @Test
    fun `end of middle copy does not jump`() {
        assertEquals(0, wrapShift(realCount * 2 - 1, realCount))
    }

    @Test
    fun `start of last copy jumps back one copy`() {
        assertEquals(-realCount, wrapShift(realCount * 2, realCount))
    }

    @Test
    fun `end of last copy jumps back one copy`() {
        assertEquals(-realCount, wrapShift(realCount * 3 - 1, realCount))
    }
}
