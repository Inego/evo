package inego.evo

import org.junit.jupiter.api.Assertions.*


fun <T> Iterator<T>.assertNext(nextValue: T?) {

    if (nextValue == null) {
        assertFalse(hasNext())
    }
    else {
        assertTrue(hasNext())
        assertEquals(nextValue, next())
    }
}