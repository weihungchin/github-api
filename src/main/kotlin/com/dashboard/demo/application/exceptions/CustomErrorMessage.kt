package com.dashboard.demo.application.exceptions

data class CustomErrorMessage(
    val codes: List<String>,
    val description: String? = null
)
