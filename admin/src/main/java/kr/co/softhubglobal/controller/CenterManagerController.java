package kr.co.softhubglobal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.softhubglobal.dto.center.CenterDTO;
import kr.co.softhubglobal.exception.ApiError;
import kr.co.softhubglobal.service.CenterManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static kr.co.softhubglobal.config.OpenApiConfig.BEARER_KEY_SECURITY_SCHEME;

@RestController
@RequestMapping("/admin/api/v1/center/managers")
@Tag(name = "Center manager", description = "Center manager APIs")
@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)
@RequiredArgsConstructor
public class CenterManagerController {

    private final CenterManagerService centerManagerService;

    @Operation(summary = "Retrieve all center manager records")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CenterDTO.CenterManagerInfo.class)) }
            )
    })
    @GetMapping
    public ResponseEntity<?> getAllCenterManagers(CenterDTO.CenterManagerSearchRequest centerManagerSearchRequest) {
        return new ResponseEntity<>(
                centerManagerService.getAllCenterManagers(centerManagerSearchRequest),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Retrieve a center manager info by its ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CenterDTO.CenterManagerDetailInfo.class)) }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT FOUND",
                    content = { @Content(schema = @Schema(implementation = ApiError.class)) }
            )
    })
    @GetMapping("{id}")
    public ResponseEntity<?> getCenterManagerById(
            @PathVariable("id") Long centerManagerId
    ) {
        return new ResponseEntity<>(
                centerManagerService.getCenterManagerById(centerManagerId),
                HttpStatus.OK
        );
    }
}
