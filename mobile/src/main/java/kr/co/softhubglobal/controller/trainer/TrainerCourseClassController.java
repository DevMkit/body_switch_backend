package kr.co.softhubglobal.controller.trainer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.softhubglobal.dto.course.CourseClassDTO;
import kr.co.softhubglobal.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static kr.co.softhubglobal.config.OpenApiConfig.BEARER_KEY_SECURITY_SCHEME;

@RestController
@RequestMapping("/trainer/api/v1/course/classes")
@Tag(name = "Trainer course class", description = "Trainer course class APIs")
@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)
@RequiredArgsConstructor
@PreAuthorize("hasRole('EMPLOYEE')")
public class TrainerCourseClassController {

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
                "",
                HttpStatus.OK
        );
    }
}
