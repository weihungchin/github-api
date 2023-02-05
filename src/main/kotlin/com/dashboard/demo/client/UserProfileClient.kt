package com.dashboard.demo.client

import com.dashboard.demo.application.domain.UserProfile

interface UserProfileClient {
    fun getTopContributors(repoOwner: String, repoName: String, pageSize: Int): List<UserProfile>

    fun getProfileDetails(userProfile: UserProfile): UserProfile
}