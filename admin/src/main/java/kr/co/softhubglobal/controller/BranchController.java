package kr.co.softhubglobal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.softhubglobal.dto.ResponseDTO;
import kr.co.softhubglobal.dto.branch.BranchDTO;
import kr.co.softhubglobal.exception.ApiError;
import kr.co.softhubglobal.service.BranchService;
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
@RequestMapping("/admin/api/v1/branches")
@Tag(name = "Center branch", description = "Center branch APIs")
@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;
    private final MessageSource messageSource;

    @Operation(summary = "Retrieve all center branch records")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BranchDTO.BranchInfo.class)) }
            )
    })
    @GetMapping
    public ResponseEntity<?> getAllBranches() {
        return new ResponseEntity<>(
                branchService.getAllBranches(),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Create a center branch record")
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
    public ResponseEntity<?> createBranch(
            @RequestBody BranchDTO.BranchCreateRequest branchCreateRequest
    ) {
        branchService.createBranch(branchCreateRequest);
        return new ResponseEntity<>(
                new ResponseDTO(messageSource.getMessage("success.create",  null, Locale.ENGLISH)),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Update a branch record by its id")
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
    @PutMapping("{branchId}")
    public ResponseEntity<ResponseDTO> updateMemberById(
            @PathVariable("branchId") Long branchId,
            @RequestBody BranchDTO.BranchUpdateRequest branchUpdateRequest
    ) {
        branchService.updateBranchById(branchId, branchUpdateRequest);
        return new ResponseEntity<>(
                new ResponseDTO(messageSource.getMessage("success.update", null, Locale.ENGLISH)),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Create a branch exercise room")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "CREATED",
                    content = {@Content(schema = @Schema(implementation = ResponseDTO.class))}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT FOUND",
                    content = {@Content(schema = @Schema(implementation = ApiError.class))}
            )
    })
    @PostMapping("/{branchId}/rooms")
    public ResponseEntity<?> createBranchExerciseRoom(
            @PathVariable("branchId") Long branchId,
            @RequestBody BranchDTO.BranchExerciseRoomCreateRequest branchExerciseRoomCreateRequest
    ) {
        branchService.createBranchExerciseRoom(branchId, branchExerciseRoomCreateRequest);
        return new ResponseEntity<>(
                new ResponseDTO(messageSource.getMessage("success.create",  null, Locale.ENGLISH)),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Create a branch product category")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "CREATED",
                    content = {@Content(schema = @Schema(implementation = ResponseDTO.class))}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT FOUND",
                    content = {@Content(schema = @Schema(implementation = ApiError.class))}
            )
    })
    @PostMapping("/{branchId}/product/category")
    public ResponseEntity<?> createBranchProductCategory(
            @PathVariable("branchId") Long branchId,
            @RequestBody BranchDTO.BranchProductCategoryCreateRequest branchProductCategoryCreateRequest
    ) {
        branchService.createBranchProductCategory(branchId, branchProductCategoryCreateRequest);
        return new ResponseEntity<>(
                new ResponseDTO(messageSource.getMessage("success.create",  null, Locale.ENGLISH)),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Create a branch product category")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "CREATED",
                    content = {@Content(schema = @Schema(implementation = ResponseDTO.class))}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT FOUND",
                    content = {@Content(schema = @Schema(implementation = ApiError.class))}
            )
    })
    @PostMapping("/{branchId}/product")
    public ResponseEntity<?> createBranchProduct(
            @PathVariable("branchId") Long branchId,
            @RequestBody BranchDTO.BranchProductCreateRequest branchProductCreateRequest
    ) {
        branchService.createBranchProduct(branchId, branchProductCreateRequest);
        return new ResponseEntity<>(
                new ResponseDTO(messageSource.getMessage("success.create",  null, Locale.ENGLISH)),
                HttpStatus.CREATED
        );
    }
}
