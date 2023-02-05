package com.dashboard.demo.application.domain

data class UserProfile(
    val id: Int,
    val userName: String,
    val numberOfCommit: Int,
    val profileUrl: String,
    var location: String? = null
)

