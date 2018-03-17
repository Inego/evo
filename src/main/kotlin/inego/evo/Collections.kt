package inego.evo

import java.util.*

fun <T> Iterable<T>.each(action: T.() -> Unit) {
    for (element in this)  element.action()
}

fun <T> MutableList<T>.removeLast() = removeAt(size - 1)

fun <T> Random.from(list: List<T>): T =
        list[this.nextInt(list.size)]
