package com.duartbreedt.radialgraph.extensions

fun <T> MutableList<T>.addIf(condition: Boolean, element: T) {
    if (condition) {
        add(element)
    }
}