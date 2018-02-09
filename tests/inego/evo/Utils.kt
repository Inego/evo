package inego.evo

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse


fun <T> Iterator<T>.assertNext(nextValue: T?) {

    if (nextValue == null) {
        assertFalse(hasNext())
    }
    else {
        assertEquals(nextValue, next())
    }
}