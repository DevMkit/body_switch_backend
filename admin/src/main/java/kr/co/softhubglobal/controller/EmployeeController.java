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
import kr.co.softhubglobal.dto.employee.EmployeeDTO;
import kr.co.softhubglobal.entity.user.User;
import kr.co.softhubglobal.exception.ApiError;
import kr.co.softhubglobal.service.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static kr.co.softhubglobal.config.OpenApiConfig.BEARER_KEY_SECURITY_SCHEME;

@RestController
@RequestMapping("/admin/api/v1/employees")
@Tag(name = "Employee", description = "Branch employee APIs")
@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final MessageSource messageSource;

    @Operation(summary = "Retrieve all employee records")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.EmployeeInfo.class)) }
            )
    })
    @GetMapping
    public ResponseEntity<?> getAllEmployees(EmployeeDTO.EmployeeSearchRequest employeeSearchRequest) {
        return new ResponseEntity<>(
                employeeService.getAllEmployees(employeeSearchRequest),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Retrieve a employee info by its ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.EmployeeDetailInfo.class)) }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT FOUND",
                    content = { @Content(schema = @Schema(implementation = ApiError.class)) }
            )
    })
    @GetMapping("{id}")
    public ResponseEntity<?> getEmployeeDetailInfoById(
            @PathVariable("id") Long employeeId
    ) {
        return new ResponseEntity<>(
                employeeService.getEmployeeDetailInfoById(employeeId),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Create a employee record")
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
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createEmployee(
            @ModelAttribute EmployeeDTO.EmployeeCreateRequest employeeCreateRequest
    ) {
        employeeService.createEmployee(employeeCreateRequest);
        return new ResponseEntity<>(
                new ResponseDTO(messageSource.getMessage("success.create",  null, Locale.ENGLISH)),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Update employee record by its id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = { @Content(schema = @Schema(implementation = ResponseDTO.class)) }
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
            )
    })
    @PutMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> updateEmployeeById(
            @PathVariable("id") Long employeeId,
            @RequestBody EmployeeDTO.EmployeeUpdateRequest employeeUpdateRequest
    ) {
        employeeService.updateEmployeeById(employeeId, employeeUpdateRequest);
        return new ResponseEntity<>(
                new ResponseDTO(messageSource.getMessage("success.update", null, Locale.ENGLISH)),
                HttpStatus.OK
        );
    }
}
