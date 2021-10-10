package com.technzone.minibursa.utils.twilio.chat.listeners

interface TaskCompletionListener<T, U> {
    fun onSuccess(t: T)
    fun onError(u: U)
}