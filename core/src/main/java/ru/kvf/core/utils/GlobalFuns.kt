package ru.kvf.core.utils

import java.util.SortedMap

suspend fun <K, V>SortedMap<K, V>.update(from: Map<out K, V>) {
    clear()
    putAll(from)
}
