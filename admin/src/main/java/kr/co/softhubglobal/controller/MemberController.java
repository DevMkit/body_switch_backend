package kr.co.softhubglobal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.softhubglobal.dto.employee.EmployeeDTO;
import kr.co.softhubglobal.dto.member.MemberDTO;
import kr.co.softhubglobal.exception.ApiError;
import kr.co.softhubglobal.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.co.softhubglobal.config.OpenApiConfig.BEARER_KEY_SECURITY_SCHEME;

@RestController
@RequestMapping("/admin/api/v1/members")
@Tag(name = "Member", description = "Member APIs")
@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "Retrieve all member records")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MemberDTO.MemberInfo.class)) }
            )
    })
    @GetMapping
    public ResponseEntity<?> getAllMembers(MemberDTO.MemberSearchRequest memberSearchRequest) {
        return new ResponseEntity<>(
                memberService.getAllMembers(memberSearchRequest),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Retrieve a member info by its ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MemberDTO.MemberDetailInfo.class)) }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT FOUND",
                    content = { @Content(schema = @Schema(implementation = ApiError.class)) }
            )
    })
    @GetMapping("{id}")
    public ResponseEntity<?> getMemberDetailInfoById(
            @PathVariable("id") String memberId
    ) {
        return new ResponseEntity<>(
                memberService.getMemberDetailInfoById(memberId),
                HttpStatus.OK
        );
    }

//    @Operation(summary = "Create a new member record")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "201",
//                    description = "CREATED",
//                    content = {@Content(schema = @Schema(implementation = ResponseDTO.class))}
//            ),
//            @ApiResponse(
//                    responseCode = "409",
//                    description = "CONFLICT",
//                    content = {@Content(schema = @Schema(implementation = ApiError.class))}
//            )
//    })
//    @PostMapping("/register")
//    public ResponseEntity<?> createMember(
//            @RequestBody MemberDTO.MemberCreateRequest memberCreateRequest
//    ) {
////        memberService.createMemberRecord(memberCreateRequest);
//        return new ResponseEntity<>(
//                "new ResponseDTO(messageSource.getMessage(\"success.create\",  null, Locale.ENGLISH))",
//                HttpStatus.CREATED
//        );
//    }
}
