package com.dashboard.demo.application.common

class KeyValueCache<T> {

    private val buffer = linkedMapOf<Int, T>()

    fun put(key: Int, value: T): T = synchronized(buffer) {
        buffer[key] = value
        return value
    }

    fun get(key: Int): T? = synchronized(buffer) {
        return buffer[key]
    }
}