package kr.co.softhubglobal.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.softhubglobal.dto.ResponseDTO;
import kr.co.softhubglobal.dto.course.CourseTicketDTO;
import kr.co.softhubglobal.entity.user.User;
import kr.co.softhubglobal.exception.ApiError;
import kr.co.softhubglobal.service.CourseTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static kr.co.softhubglobal.config.OpenApiConfig.BEARER_KEY_SECURITY_SCHEME;

@RestController
@RequestMapping("/user/api/v1/course/tickets")
@Tag(name = "Course Ticket", description = "Course ticket APIs")
@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)
@RequiredArgsConstructor
@PreAuthorize("hasRole('MEMBER')")
public class CourseTicketController {

    private final CourseTicketService courseTicketService;
    private final MessageSource messageSource;

    @Operation(summary = "Retrieve course ticket records")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CourseTicketDTO.CourseTicketInfo.class)) }
            )
    })
    @GetMapping
    public ResponseEntity<?> getCourseTickets(
            @AuthenticationPrincipal UserDetails userDetails,
            CourseTicketDTO.CourseTicketSearchRequest courseTicketSearchRequest
    ) {
        return new ResponseEntity<>(
                courseTicketService.getCourseTickets(((User) userDetails).getId(), courseTicketSearchRequest),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Retrieve a course ticket detail info by its ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CourseTicketDTO.CourseTicketDetailInfo.class)) }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT FOUND",
                    content = { @Content(schema = @Schema(implementation = ApiError.class)) }
            )
    })
    @GetMapping("{courseTicketId}")
    public ResponseEntity<?> getCourseTicketDetailInfoById(
            @PathVariable("courseTicketId") Long courseTicketId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return new ResponseEntity<>(
                courseTicketService.getCourseTicketDetailInfoById(courseTicketId, ((User) userDetails).getId()),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Save course ticket by its ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)) }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT FOUND",
                    content = { @Content(schema = @Schema(implementation = ApiError.class)) }
            )
    })
    @PostMapping("{courseTicketId}/save")
    public ResponseEntity<?> saveCourseTicketById(
            @PathVariable("courseTicketId") Long courseTicketId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        courseTicketService.saveCourseTicketById(courseTicketId, ((User) userDetails).getId());
        return new ResponseEntity<>(
                new ResponseDTO(messageSource.getMessage("success.update", null, Locale.ENGLISH)),
                HttpStatus.OK
        );
    }
}
