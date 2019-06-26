package com.ankit.mishra.Data

interface Response<S, E> {

    companion object {
        const val HTTP_OK = 200
    }
    val httpResponseCode: Int
    fun successResponse(): S
    fun erroResponse(): E
    fun isHttpOk() = HTTP_OK == httpResponseCode
}