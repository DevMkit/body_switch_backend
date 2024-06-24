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
import kr.co.softhubglobal.entity.center.CenterManagerStatus;
import kr.co.softhubglobal.entity.center.CenterType;
import kr.co.softhubglobal.entity.user.User;
import kr.co.softhubglobal.exception.ApiError;
import kr.co.softhubglobal.service.CenterManagerService;
import kr.co.softhubglobal.service.CenterService;
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
@RequestMapping("/admin/api/v1/centers")
@Tag(name = "Center", description = "Center APIs")
@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)
@RequiredArgsConstructor
public class CenterController {

    private final CenterService centerService;
    private final CenterManagerService centerManagerService;
    private final MessageSource messageSource;

    @Operation(summary = "Retrieve all center records")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CenterDTO.CenterInfo.class)) }
            )
    })
    @GetMapping
    public ResponseEntity<?> getAllCenters(
            CenterDTO.CenterSearchRequest centerSearchRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return new ResponseEntity<>(
                centerService.getAllCenterBranches(centerSearchRequest, (User) userDetails),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Create a center record")
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
    @PostMapping
    public ResponseEntity<?> createCenter(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CenterDTO.CenterManagerCreateRequest centerManagerCreateRequest
    ) {
        centerManagerService.createCenterManager(
                centerManagerCreateRequest,
                CenterManagerStatus.APPROVED,
                CenterType.BRANCH,
                ((User) userDetails).getId()
        );
        return new ResponseEntity<>(
                new ResponseDTO(messageSource.getMessage("success.create",  null, Locale.ENGLISH)),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Retrieve a center detail info by its ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CenterDTO.CenterDetailInfo.class)) }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT FOUND",
                    content = { @Content(schema = @Schema(implementation = ApiError.class)) }
            )
    })
    @GetMapping("{id}")
    public ResponseEntity<?> getCenterDetailInfoById(
            @PathVariable("id") Long centerId
    ) {
        return new ResponseEntity<>(
                centerService.getCenterDetailInfoById(centerId),
                HttpStatus.OK
        );
    }
}
