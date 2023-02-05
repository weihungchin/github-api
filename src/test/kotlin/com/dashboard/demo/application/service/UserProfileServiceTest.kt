package com.dashboard.demo.application.service

import com.dashboard.demo.application.domain.UserProfile
import com.dashboard.demo.client.UserProfileClient
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.timeout
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@ExtendWith(MockitoExtension::class)
class UserProfileServiceTest {

    @Mock()
    private lateinit var userProfileClientMock: UserProfileClient

    @InjectMocks
    private lateinit var cut: UserProfileService

    @Test
    fun `should return a list of contributors sorted by number of commits if no parameter is provided`(){
        val userProfile1 = createUserProfile().copy(id = 1, numberOfCommit = 10)
        val userProfile2 = createUserProfile().copy(id = 2, numberOfCommit = 100)
        val expectedContributors = listOf(
            userProfile1,
            userProfile2
        )
        val expectedSorted = expectedContributors.sortedByDescending { it.numberOfCommit }

        whenever(userProfileClientMock.getTopContributors(any(), any(), any())).thenReturn(expectedSorted)
        whenever(userProfileClientMock.getProfileDetails(eq(userProfile1))).thenReturn(userProfile1)
        whenever(userProfileClientMock.getProfileDetails(eq(userProfile2))).thenReturn(userProfile2)

        val actual = cut.getTopContributorsFor(null, null, null)

        assertEquals(expectedSorted, actual)
        verify(userProfileClientMock, times(1)).getProfileDetails(userProfile1)
        verify(userProfileClientMock, times(1)).getProfileDetails(userProfile2)
        verify(userProfileClientMock, times(1)).getTopContributors(any(), any(), any())
    }

    @Test
    fun `should get the location of contributors if it is available`(){
        val userProfile1 = createUserProfile().copy(id = 1, numberOfCommit = 10, location = null)
        val expectedLocation = "Stuggart"
        val expectedContributors = listOf(
            userProfile1,
        )

        whenever(userProfileClientMock.getTopContributors(any(), any(), any())).thenReturn(expectedContributors)
        whenever(userProfileClientMock.getProfileDetails(eq(userProfile1))).thenReturn(userProfile1.copy(location = expectedLocation))

        val actual = cut.getTopContributorsFor(null, null, null)

        assertEquals(expectedLocation, actual.first().location)
    }


    @Test
    fun `should return a list of contributors following the default size of 25 if no parameter is provided`(){
        val userProfile1 = createUserProfile().copy(id = 1, numberOfCommit = 10)
        val expectedSize = 25

        whenever(userProfileClientMock.getTopContributors(any(), any(), any())).thenReturn(listOf(userProfile1))
        whenever(userProfileClientMock.getProfileDetails(eq(userProfile1))).thenReturn(userProfile1)
        cut.getTopContributorsFor(null, null, null)

        verify(userProfileClientMock, times(1)).getTopContributors(any(), any(), eq(expectedSize))
    }

    @Test
    fun `should return a list of contributors following the pageSize parameter if it is provided`(){
        val userProfile1 = createUserProfile().copy(id = 1, numberOfCommit = 10)
        val expectedSize = 1

        whenever(userProfileClientMock.getTopContributors(any(), any(), any())).thenReturn(listOf(userProfile1))
        whenever(userProfileClientMock.getProfileDetails(eq(userProfile1))).thenReturn(userProfile1)
        cut.getTopContributorsFor(null, null, expectedSize)

        verify(userProfileClientMock, times(1)).getTopContributors(any(), any(), eq(expectedSize))
    }

    @Test
    fun `should return a list of contributors following the repoName and repoOwner parameter provided`(){
        val userProfile1 = createUserProfile().copy(id = 1, numberOfCommit = 10)
        val expectedRepoName = "scala"
        val expectedOwnerName = "name"

        whenever(userProfileClientMock.getTopContributors(any(), any(), any())).thenReturn(listOf(userProfile1))
        whenever(userProfileClientMock.getProfileDetails(eq(userProfile1))).thenReturn(userProfile1)
        cut.getTopContributorsFor(repoOwner = expectedOwnerName, repoName = expectedRepoName, null)

        verify(userProfileClientMock, times(1)).getTopContributors(eq(expectedOwnerName), eq(expectedRepoName), any())
    }

    @Nested
    inner class GetInfoFromCache{
        val userProfile1 = createUserProfile().copy(id = 1, numberOfCommit = 10)
        val expectedContributors = listOf(
            userProfile1,
        )
        @BeforeEach
        fun setup(){
            whenever(userProfileClientMock.getTopContributors(any(), any(), any())).thenReturn(expectedContributors)
            whenever(userProfileClientMock.getProfileDetails(any())).thenReturn(userProfile1)
            cut.getTopContributorsFor(null, null, null)
        }

        @Test
        fun `should get user detail data from cache if it is available in cache`(){

            verify(userProfileClientMock, times(1)).getProfileDetails(userProfile1)

            cut.getTopContributorsFor(null, null, null)
            val actual = cut.getTopContributorsFor(null, null, null)

            verify(userProfileClientMock, times(1)).getProfileDetails(userProfile1)
            assertEquals(expectedContributors, actual)
        }
    }

    private fun createUserProfile(): UserProfile {
        return UserProfile(
            id = (0..10000).random(),
            userName = "userName",
            numberOfCommit = 10,
            profileUrl = "profileUrl",
        )
    }
}