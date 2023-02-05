package com.dashboard.demo.application.exceptions

import org.springframework.core.Constants.ConstantException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.RestClientException
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(
        CustomBadRequestException::class,
    )
    fun handleBadRequestExceptions(e: CustomBadRequestException): ResponseEntity<CustomErrorMessage> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CustomErrorMessage(e.codes, e.cause?.message))
    }

    @ExceptionHandler(
        CustomServerException::class,
    )
    fun handleExternalServiceExceptions(e: CustomBadRequestException): ResponseEntity<CustomErrorMessage> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CustomErrorMessage(e.codes, e.message))
    }

    @ExceptionHandler(RestClientException::class)
    fun handleRestClientException(e: Exception): ResponseEntity<CustomErrorMessage> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(e: ConstraintViolationException): ResponseEntity<CustomErrorMessage> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CustomErrorMessage(listOf(ErrorCode.INVALID_PARAMETER.code), e.message))
    }
}

enum class ErrorCode(val code: String, var description: String) {
    EXTERNAL_CONNECTION_ERROR("EXTERNAL_CONNECTION_ERROR", "Failed to connect to external system"),
    INVALID_PARAMETER("INVALID_PARAMETER", "Invalid parameter")
}