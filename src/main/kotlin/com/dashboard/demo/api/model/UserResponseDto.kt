package com.dashboard.demo.api.model

import com.dashboard.demo.application.domain.UserProfile

data class UserResponseDto(
    val username: String,
    val id: Int,
    val numberOfCommit:Int,
    val location: String?
)

fun UserProfile.toResponseDto() = UserResponseDto(
    username = this.userName,
    id = this.id,
    numberOfCommit = this.numberOfCommit,
    location = this.location
)
