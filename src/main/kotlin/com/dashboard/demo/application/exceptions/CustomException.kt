package com.dashboard.demo.application.exceptions

sealed class CustomException(
    message: String? = null,
    val codes: List<String> = emptyList(),
    cause: Exception? = null
) : RuntimeException(message, cause)

class CustomBadRequestException(message: String?, codes: List<ErrorCode>, cause: Exception? = null) : CustomException(
    message = message,
    codes = codes.map { it.code },
    cause = cause
)

class CustomServerException(message: String?, codes: List<ErrorCode>, cause: Exception? = null) : CustomException(
    message = message,
    codes = codes.map { it.code },
    cause = cause
)
