package com.bonghwan.mosquito.util

class EventWrapper<T>(content: T?) {
    private val mContent: T     // 현재 들어온 값
    private var hasBeenHandled = false  // 예전에 다루어진 값인가?

    val contentIfNotHandled: T?
        get() = if (hasBeenHandled) null else {
            hasBeenHandled = true
            mContent
        }

    init {
        requireNotNull(content) {"null values in Event are not allowed."}
        mContent = content
    }

    fun peekContent(): T {
        return mContent
    }

    fun hasBeenHandled(): Boolean {
        return hasBeenHandled
    }
}