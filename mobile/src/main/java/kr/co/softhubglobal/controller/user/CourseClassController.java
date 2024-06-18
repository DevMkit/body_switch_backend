package kr.co.softhubglobal.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.softhubglobal.dto.ResponseDTO;
import kr.co.softhubglobal.dto.course.CourseClassDTO;
import kr.co.softhubglobal.entity.user.User;
import kr.co.softhubglobal.exception.ApiError;
import kr.co.softhubglobal.service.CourseClassService;
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
@RequestMapping("/user/api/v1/course/classes")
@Tag(name = "Course Class", description = "Course Class APIs")
@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)
@RequiredArgsConstructor
@PreAuthorize("hasRole('MEMBER')")
public class CourseClassController {

    private final CourseClassService courseClassService;
    private final MessageSource messageSource;

    @Operation(summary = "Retrieve course class records")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CourseClassDTO.CourseClassTimeInfo.class)) }
            )
    })
    @GetMapping
    public ResponseEntity<?> getCourseClasses(
            @AuthenticationPrincipal UserDetails userDetails,
            CourseClassDTO.CourseClassSearchRequest courseClassSearchRequest
    ) {
        return new ResponseEntity<>(
                courseClassService.getCourseClasses(((User) userDetails).getId(), courseClassSearchRequest),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Retrieve a course class detail info by its ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CourseClassDTO.CourseClassTimeDetailInfo.class)) }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT FOUND",
                    content = { @Content(schema = @Schema(implementation = ApiError.class)) }
            )
    })
    @GetMapping("{courseClassTimeId}")
    public ResponseEntity<?> getCourseTicketDetailInfoById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("courseClassTimeId") Long courseClassTimeId
    ) {
        return new ResponseEntity<>(
                courseClassService.getCourseTicketDetailInfoById(((User) userDetails).getId(), courseClassTimeId),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Reserve a course class time")
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
            ),
            @ApiResponse(
                    responseCode = "406",
                    description = "NOT ACCEPTABLE",
                    content = { @Content(schema = @Schema(implementation = ApiError.class)) }
            )
    })
    @PostMapping("/reserve")
    public ResponseEntity<?> reserveCourseClassTime(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CourseClassDTO.CourseClassTimeReserveRequest courseClassTimeReserveRequest
    ) {
        courseClassService.reserveCourseClassTime(((User) userDetails).getId(), courseClassTimeReserveRequest);
        return new ResponseEntity<>(
                new ResponseDTO(messageSource.getMessage("success.reserved", null, Locale.ENGLISH)),
                HttpStatus.OK
        );
    }
}
