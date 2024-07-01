package kr.co.softhubglobal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import kr.co.softhubglobal.dto.searchOption.SearchOptionDTO;
import kr.co.softhubglobal.entity.user.User;
import kr.co.softhubglobal.service.SearchOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static kr.co.softhubglobal.config.OpenApiConfig.BEARER_KEY_SECURITY_SCHEME;

@RestController
@RequestMapping("/admin/api/v1/search-options")
@Tag(name = "Search option", description = "Search option APIs")
@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)
@RequiredArgsConstructor
public class SearchOptionController {

    private final SearchOptionService searchOptionService;

    @Operation(summary = "Retrieve head branch selection options")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = SearchOptionDTO.HeadBranchSelectionInfo.class)) }
            )
    })
    @GetMapping("head-branches")
    public ResponseEntity<?> getHeadBranchSelectionOptions(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(
                searchOptionService.getHeadBranchSelectionOptions((User) userDetails),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Retrieve branch selection options")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = SearchOptionDTO.BranchSelectionInfo.class)) }
            )
    })
    @GetMapping("branches")
    public ResponseEntity<?> getBranchSelectionOptions(
            @AuthenticationPrincipal UserDetails userDetails,
            Long headBranchId
    ) {
        return new ResponseEntity<>(
                searchOptionService.getBranchSelectionOptions((User) userDetails, headBranchId),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Retrieve course ticket selection options")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = SearchOptionDTO.CourseTicketSelectionInfo.class)) }
            )
    })
    @GetMapping("course-tickets")
    public ResponseEntity<?> getCourseTicketSelectionOptions(
            Long branchId
    ) {
        return new ResponseEntity<>(
                searchOptionService.getCourseTicketSelectionOptions(branchId),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Retrieve course ticket trainer selection options")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = SearchOptionDTO.CourseTicketTrainerSelectionInfo.class)) }
            )
    })
    @GetMapping("course-trainers")
    public ResponseEntity<?> getCourseTicketTrainerSelectionOptions(
            Long courseTicketId
    ) {
        return new ResponseEntity<>(
                searchOptionService.getCourseTicketTrainerSelectionOptions(courseTicketId),
                HttpStatus.OK
        );
    }
}
