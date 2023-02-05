package com.dashboard.demo.api.controller

import com.dashboard.demo.api.model.UserResponseDto
import com.dashboard.demo.api.model.toResponseDto
import com.dashboard.demo.application.service.UserProfileService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.validation.constraints.Max


@RestController
@RequestMapping("/api")
@Validated
class ContributorController(
    private val userProfileService: UserProfileService,
) {

    @GetMapping("/repos/contributors")
    fun getTopContributor(
        @RequestParam("repoOwner", required = false) repoOwner: String?,
        @RequestParam("repoName", required = false) repoName: String?,
        @RequestParam("pageSize", required = false) @Max(value = 25, message = "Must be an integer not more than 25") pageSize: Int?
    ): ResponseEntity<List<UserResponseDto>> {
        val result = this.userProfileService.getTopContributorsFor(
            repoOwner = repoOwner,
            repoName = repoName,
            pageSize = pageSize
        ).map { it.toResponseDto() }

        return ResponseEntity.status(HttpStatus.OK).body(result)
    }
}