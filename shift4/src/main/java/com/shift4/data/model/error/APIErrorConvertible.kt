package com.shift4.data.model.error

public interface APIErrorConvertible {
    fun toAPIError(): APIError
}