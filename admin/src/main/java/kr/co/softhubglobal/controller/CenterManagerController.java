package kr.co.softhubglobal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.softhubglobal.dto.ResponseDTO;
import kr.co.softhubglobal.dto.center.CenterDTO;
import kr.co.softhubglobal.entity.center.CenterType;
import kr.co.softhubglobal.entity.user.User;
import kr.co.softhubglobal.exception.ApiError;
import kr.co.softhubglobal.service.CenterManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static kr.co.softhubglobal.config.OpenApiConfig.BEARER_KEY_SECURITY_SCHEME;

@RestController
@RequestMapping("/admin/api/v1/center/managers")
@Tag(name = "Center manager", description = "Center manager APIs")
@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)
@RequiredArgsConstructor
public class CenterManagerController {

    private final CenterManagerService centerManagerService;
    private final MessageSource messageSource;

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
    public ResponseEntity<?> getCenterManagerDetailInfoById(
            @PathVariable("id") Long centerManagerId
    ) {
        return new ResponseEntity<>(
                centerManagerService.getCenterManagerDetailInfoById(centerManagerId),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Create a center manger record")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "CREATED",
                    content = {@Content(schema = @Schema(implementation = ResponseDTO.class))}
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "NOT FOUND",
                    content = {@Content(schema = @Schema(implementation = ApiError.class))}
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "NOT ACCEPTABLE",
                    content = {@Content(schema = @Schema(implementation = ApiError.class))}
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "CONFLICT",
                    content = {@Content(schema = @Schema(implementation = ApiError.class))}
            )
    })
    @PostMapping
    public ResponseEntity<?> createCenterManager(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CenterDTO.CenterManagerCreateRequest centerManagerCreateRequest
    ) {
        centerManagerService.createCenterManager(
                centerManagerCreateRequest,
                null,
                CenterType.HEAD,
                ((User) userDetails).getId()
        );
        return new ResponseEntity<>(
                new ResponseDTO(messageSource.getMessage("success.create",  null, Locale.ENGLISH)),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Update center manager record by its id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = { @Content(schema = @Schema(implementation = ResponseDTO.class)) }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT FOUND",
                    content = { @Content(schema = @Schema(implementation = ApiError.class)) }
            )
    })
    @PutMapping("{id}")
    public ResponseEntity<ResponseDTO> updateCenterManagerById(
            @PathVariable("id") Long centerManagerId,
            @RequestBody CenterDTO.CenterManagerUpdateRequest centerManagerUpdateRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        centerManagerService.updateCenterManagerById(centerManagerId, centerManagerUpdateRequest, ((User) userDetails).getId());
        return new ResponseEntity<>(
                new ResponseDTO(messageSource.getMessage("success.update", null, Locale.ENGLISH)),
                HttpStatus.OK
        );
    }
}
