fun <T> Iterable<T>.each(action: T.() -> Unit) {
    for (element in this)  element.action()
}
