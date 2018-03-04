package inego.evo

import java.util.*
import java.util.concurrent.ThreadLocalRandom

fun <T> Iterable<T>.each(action: T.() -> Unit) {
    for (element in this)  element.action()
}

fun <T> MutableList<T>.removeLast() = removeAt(size - 1)

val <T> List<T>.randomElement
    get(): T = this[ThreadLocalRandom.current().nextInt(size)]

fun <T> Random.from(list: List<T>): T =
        list[this.nextInt(list.size)]