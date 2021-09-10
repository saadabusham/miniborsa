package com.technzone.miniborsa.utils.twilio.chat.listeners

interface TaskCompletionListener<T, U> {
    fun onSuccess(t: T)
    fun onError(u: U)
}