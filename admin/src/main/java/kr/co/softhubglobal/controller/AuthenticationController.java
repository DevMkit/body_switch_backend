package kr.co.softhubglobal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.softhubglobal.dto.AuthenticationDTO;
import kr.co.softhubglobal.dto.ResponseDTO;
import kr.co.softhubglobal.dto.center.CenterDTO;
import kr.co.softhubglobal.entity.center.CenterManagerStatus;
import kr.co.softhubglobal.entity.center.CenterType;
import kr.co.softhubglobal.exception.ApiError;
import kr.co.softhubglobal.service.AuthenticationService;
import kr.co.softhubglobal.service.CenterManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/admin/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Login APIs")
public class AuthenticationController {

    private final CenterManagerService centerManagerService;
    private final AuthenticationService authenticationService;
    private final MessageSource messageSource;

    @Operation(summary = "Login(username, password)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = { @Content(schema = @Schema(implementation = AuthenticationDTO.AuthenticationResponse.class)) }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "BAD REQUEST",
                    content = { @Content(schema = @Schema(implementation = ApiError.class)) }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "UNAUTHORIZED",
                    content = { @Content(schema = @Schema(implementation = ApiError.class)) }
            )
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationDTO.AuthenticationRequest request) {
        return new ResponseEntity<>(
                authenticationService.login(request),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Create a center manager record")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "CREATED",
                    content = {@Content(schema = @Schema(implementation = ResponseDTO.class))}
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "CONFLICT",
                    content = {@Content(schema = @Schema(implementation = ApiError.class))}
            )
    })
    @PostMapping("/register")
    public ResponseEntity<?> createCenterManager(
            @RequestBody CenterDTO.CenterManagerCreateRequest centerManagerCreateRequest
    ) {
        centerManagerService.createCenterManager(
                centerManagerCreateRequest,
                CenterManagerStatus.APPROVAL_PENDING,
                CenterType.HEAD,
                null
        );
        return new ResponseEntity<>(
                new ResponseDTO(messageSource.getMessage("success.create",  null, Locale.ENGLISH)),
                HttpStatus.CREATED
        );
    }
}
