import java.util.concurrent.ThreadLocalRandom

fun <T> Iterable<T>.each(action: T.() -> Unit) {
    for (element in this)  element.action()
}

fun <T> MutableList<T>.removeLast() = removeAt(size - 1)

fun <T> List<T>.getRandomElement(): T = this[ThreadLocalRandom.current().nextInt(size)]
