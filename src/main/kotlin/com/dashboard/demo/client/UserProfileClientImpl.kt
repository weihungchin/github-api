package com.dashboard.demo.client

import com.dashboard.demo.application.domain.UserProfile
import com.dashboard.demo.application.exceptions.CustomBadRequestException
import com.dashboard.demo.application.exceptions.CustomServerException
import com.dashboard.demo.application.exceptions.ErrorCode
import com.dashboard.demo.client.common.RestClientResolver
import com.dashboard.demo.client.model.UserClientDto
import com.dashboard.demo.client.model.UserDetailClientDto
import com.dashboard.demo.client.model.toDomain
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException

@Component
class UserProfileClientImpl(
    private val restClientResolver: RestClientResolver,
    @Value("\${app.github.base.url}")
    private val githubBaseUrl: String,
): UserProfileClient {

    override fun getTopContributors(repoOwner: String, repoName: String, pageSize: Int): List<UserProfile> {
        val url = "${githubBaseUrl}/repos/${repoOwner}/${repoName}/contributors?per_page=${pageSize}"
        val response = fetchRawData(url)
        val mappedResult = ObjectMapper().readValue(response, object : TypeReference<List<UserClientDto>>() {})
        return mappedResult.map { it.toDomain() }
    }

    override fun getProfileDetails(userProfile: UserProfile): UserProfile {
        val url = "${githubBaseUrl}/users/${userProfile.userName}"
        val response = fetchRawData(url)
        val mappedResult: UserDetailClientDto? = ObjectMapper().readValue(response, UserDetailClientDto::class.java)
        if(mappedResult != null) return mappedResult.toDomain(userProfile.numberOfCommit)
        return userProfile
    }

    private fun fetchRawData(url: String): String? {
        val response: String?
        try {
            response = restClientResolver.get(url)
        } catch (ex: HttpClientErrorException){
            throw CustomBadRequestException(
                message = ErrorCode.INVALID_PARAMETER.code,
                codes = listOf(ErrorCode.INVALID_PARAMETER),
                cause = ex
            )
        }
        catch (ex: Exception){
            throw CustomServerException(
                message = ErrorCode.EXTERNAL_CONNECTION_ERROR.description,
                codes = listOf(ErrorCode.EXTERNAL_CONNECTION_ERROR),
                cause = ex
            )
        }
        return response
    }
}