package kr.co.softhubglobal.controller.user;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.softhubglobal.dto.ResponseDTO;
import kr.co.softhubglobal.dto.member.MemberDTO;
import kr.co.softhubglobal.entity.user.User;
import kr.co.softhubglobal.exception.ApiError;
import kr.co.softhubglobal.service.MemberService;
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
@RequestMapping("/user/api/v1/members")
@Tag(name = "Member", description = "Member APIs")
@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)
@RequiredArgsConstructor
@PreAuthorize("hasRole('MEMBER')")
public class MemberController {

    private final MemberService memberService;
    private final MessageSource messageSource;

    @Operation(summary = "Retrieve all member records")
    @Hidden
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MemberDTO.MemberInfo.class)) }
            )
    })
    @GetMapping
    public ResponseEntity<?> getAllMembers() {
        return new ResponseEntity<>(
                memberService.getAllMembers(),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Retrieve member usage information")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MemberDTO.MemberUsageInfo.class)) }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT FOUND",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }
            )
    })
    @GetMapping("usage-info")
    public ResponseEntity<?> getMemberUsageInformation(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(
                memberService.getMemberUsageInformation(((User) userDetails).getId()),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Retrieve member reservation information")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MemberDTO.MemberReservationInfo.class)) }
            )
    })
    @GetMapping("reservation")
    public ResponseEntity<?> getMemberReservations(
            @AuthenticationPrincipal UserDetails userDetails,
            MemberDTO.MemberReservationSearchRequest memberReservationSearchRequest
    ) {
        return new ResponseEntity<>(
                memberService.getMemberReservations(((User) userDetails).getId(), memberReservationSearchRequest),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Retrieve member reservation detail information by its ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MemberDTO.MemberReservationDetailInfo.class)) }
            )
    })
    @GetMapping("reservation/{reservationId}")
    public ResponseEntity<?> getMemberReservationDetailInfoById(
            @PathVariable("reservationId") Long reservationId
    ) {
        return new ResponseEntity<>(
                memberService.getMemberReservationDetailInfoById(reservationId),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Cancel member reservation by its ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)) }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "BAD REQUEST",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT FOUND",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }
            )
    })
    @PostMapping("reservation/cancel")
    public ResponseEntity<?> cancelReservationById(
            @RequestBody MemberDTO.MemberReservationCancelRequest memberReservationCancelRequest
    ) {
        memberService.cancelReservationById(memberReservationCancelRequest);
        return new ResponseEntity<>(
                new ResponseDTO(messageSource.getMessage("success.canceled", null, Locale.ENGLISH)),
                HttpStatus.OK
        );
    }
}
