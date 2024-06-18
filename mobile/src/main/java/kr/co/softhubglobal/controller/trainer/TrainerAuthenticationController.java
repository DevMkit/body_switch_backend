package kr.co.softhubglobal.controller.trainer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.softhubglobal.dto.AuthenticationDTO;
import kr.co.softhubglobal.exception.ApiError;
import kr.co.softhubglobal.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainer/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Trainer authentication", description = "Trainer login APIs")
public class TrainerAuthenticationController {

    private final AuthenticationService authenticationService;

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
    public ResponseEntity<?> trainerLogin(@RequestBody AuthenticationDTO.AuthenticationRequest request) {
        return new ResponseEntity<>(
                authenticationService.trainerLogin(request),
                HttpStatus.OK
        );
    }
}
