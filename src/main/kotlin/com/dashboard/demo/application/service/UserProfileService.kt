package com.dashboard.demo.application.service

import com.dashboard.demo.application.common.KeyValueCache
import com.dashboard.demo.application.domain.UserProfile
import com.dashboard.demo.application.exceptions.CustomBadRequestException
import com.dashboard.demo.application.exceptions.ErrorCode
import com.dashboard.demo.client.UserProfileClient
import org.springframework.boot.autoconfigure.security.SecurityProperties.User
import org.springframework.stereotype.Service
import java.lang.Exception

private const val DEFAULT_REPO_OWNER = "vitejs"
private const val DEFAULT_REPO_NAME = "vite"
private const val DEFAULT_PAGE_SIZE = 25

@Service
class UserProfileService(
    private val userProfileClient: UserProfileClient
) {
    private val userProfileDetailCache = KeyValueCache<UserProfile>()

    fun getTopContributorsFor(repoOwner: String?, repoName: String?, pageSize: Int?): List<UserProfile> {
        return userProfileClient.getTopContributors(
            repoOwner = repoOwner ?: DEFAULT_REPO_OWNER,
            repoName = repoName ?: DEFAULT_REPO_NAME,
            pageSize = pageSize ?: DEFAULT_PAGE_SIZE
        ).map { getUserProfileDetailInfo(it) }
    }

    private fun getUserProfileDetailInfo(userProfile: UserProfile): UserProfile {
        val cachedUserProfileDetail: UserProfile? = userProfileDetailCache.get(userProfile.id)
        if (cachedUserProfileDetail != null) return cachedUserProfileDetail

        val userProfileDetail = userProfileClient.getProfileDetails(userProfile)
        return userProfileDetailCache.put(userProfileDetail.id, userProfileDetail)
    }
}