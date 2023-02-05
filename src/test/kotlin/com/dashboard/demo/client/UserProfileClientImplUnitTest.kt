package com.dashboard.demo.client

import com.dashboard.demo.application.domain.UserProfile
import com.dashboard.demo.application.exceptions.CustomBadRequestException
import com.dashboard.demo.client.common.RestClientResolver
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException

@ExtendWith(MockitoExtension::class)
class UserProfileClientImplUnitTest {

    @Mock
    private lateinit var restClientResolverMock: RestClientResolver

    private lateinit var cut: UserProfileClientImpl

    @BeforeEach
    fun setup() {
        cut = UserProfileClientImpl(restClientResolverMock, "url")
    }

    @Test
    fun `should throw CustomBadRequestException if restClientResolver returns HttpClientErrorException exception when getting top contributors`() {
        whenever(restClientResolverMock.get(any())).thenThrow(HttpClientErrorException(HttpStatus.BAD_REQUEST, ""))

        assertThrows<CustomBadRequestException> { cut.getTopContributors("repoOwner", "repoName", 25) }
    }

    @Test
    fun `should throw CustomBadRequestException if restClientResolver returns HttpClientErrorException exception when getting user profile data`() {
        whenever(restClientResolverMock.get(any())).thenThrow(HttpClientErrorException(HttpStatus.BAD_REQUEST, ""))
        assertThrows<CustomBadRequestException> { cut.getProfileDetails(UserProfile(1, "userName", 1, "")) }
    }

    @Test
    fun `should return a list of user if restClientResolver returns data successfully`() {
        val userProfile = UserProfile(1, "userName", 10, "profile")
        val expectedResult = listOf(userProfile)

        whenever(restClientResolverMock.get(any())).thenReturn("[{\"login\":\"${userProfile.userName}\",\"id\":${userProfile.id},\"url\":\"${userProfile.profileUrl}\", \"type\":\"User\", \"contributions\":${userProfile.numberOfCommit}}]")

        val actual = cut.getTopContributors("repoOwner", "repoName", 1)

        assertEquals(expectedResult, actual)
    }

    @Test
    fun `should return a user profile data if restClientResolver returns data successfully`() {
        val expectedResult = UserProfile(1, "userName", 10, "profile", location = "SG")

        whenever(restClientResolverMock.get(any())).thenReturn("{\"login\":\"${expectedResult.userName}\",\"id\":${expectedResult.id},\"node_id\":\"MDQ6VXNlcjQ5OTU1MA==\",\"avatar_url\":\"https://avatars.githubusercontent.com/u/499550?v=4\",\"gravatar_id\":\"\",\"url\":\"${expectedResult.profileUrl}\",\"html_url\":\"https://github.com/yyx990803\",\"followers_url\":\"https://api.github.com/users/yyx990803/followers\",\"following_url\":\"https://api.github.com/users/yyx990803/following{/other_user}\",\"gists_url\":\"https://api.github.com/users/yyx990803/gists{/gist_id}\",\"starred_url\":\"https://api.github.com/users/yyx990803/starred{/owner}{/repo}\",\"subscriptions_url\":\"https://api.github.com/users/yyx990803/subscriptions\",\"organizations_url\":\"https://api.github.com/users/yyx990803/orgs\",\"repos_url\":\"https://api.github.com/users/yyx990803/repos\",\"events_url\":\"https://api.github.com/users/yyx990803/events{/privacy}\",\"received_events_url\":\"https://api.github.com/users/yyx990803/received_events\",\"type\":\"User\",\"site_admin\":false,\"name\":\"Evan You\",\"company\":\"vuejs\",\"blog\":\"http://evanyou.me\",\"location\":\"${expectedResult.location}\",\"email\":null,\"hireable\":null,\"bio\":null,\"twitter_username\":null,\"public_repos\":182,\"public_gists\":71,\"followers\":89630,\"following\":93,\"created_at\":\"2010-11-28T01:05:40Z\",\"updated_at\":\"2023-01-21T08:56:13Z\"}")

        val actual = cut.getProfileDetails(expectedResult)

        assertEquals(expectedResult, actual)
    }
}