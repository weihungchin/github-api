package com.dashboard.demo.client.model

import com.dashboard.demo.application.domain.UserProfile
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserDetailClientDto(
    @JsonProperty("id")
    val id: Int,

    @JsonProperty("location")
    val location: String?,

    @JsonProperty("login")
    val login: String,

    @JsonProperty("url")
    val url: String,
)

fun UserDetailClientDto.toDomain(numberOfCommits: Int) = UserProfile(
    id = this.id,
    location = this.location,
    numberOfCommit = numberOfCommits,
    profileUrl = this.url,
    userName = this.login
)
