package com.dashboard.demo.client.model

import com.dashboard.demo.application.domain.UserProfile
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserClientDto(
    @JsonProperty("id")
    val id: Int,

    @JsonProperty("contributions")
    val contributions: Int,

    @JsonProperty("url")
    val url: String,

    @JsonProperty("login")
    val login: String,
)

fun UserClientDto.toDomain() = UserProfile(
    id = this.id,
    userName = this.login,
    numberOfCommit = this.contributions,
    profileUrl = this.url
)