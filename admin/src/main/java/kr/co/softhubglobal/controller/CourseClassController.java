package kr.co.softhubglobal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.softhubglobal.dto.ResponseDTO;
import kr.co.softhubglobal.dto.course.CourseTicketDTO;
import kr.co.softhubglobal.exception.ApiError;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

import static kr.co.softhubglobal.config.OpenApiConfig.BEARER_KEY_SECURITY_SCHEME;

@RestController
@RequestMapping("/admin/api/v1/course/classes")
@Tag(name = "Course class", description = "Course class APIs")
@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)
@RequiredArgsConstructor
public class CourseClassController {

    private final MessageSource messageSource;

    @Operation(summary = "Create a course class record")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "CREATED",
                    content = {@Content(schema = @Schema(implementation = ResponseDTO.class))}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "BAD REQUEST",
                    content = { @Content(schema = @Schema(implementation = ApiError.class)) }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT FOUND",
                    content = { @Content(schema = @Schema(implementation = ApiError.class)) }
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "CONFLICT",
                    content = {@Content(schema = @Schema(implementation = ApiError.class))}
            )
    })
    @PostMapping
    public ResponseEntity<?> createCourseClass(
            @RequestBody CourseTicketDTO.CourseTicketCreateRequest courseTicketCreateRequest
    ) {
        return new ResponseEntity<>(
                new ResponseDTO(messageSource.getMessage("success.create",  null, Locale.ENGLISH)),
                HttpStatus.CREATED
        );
    }
}
