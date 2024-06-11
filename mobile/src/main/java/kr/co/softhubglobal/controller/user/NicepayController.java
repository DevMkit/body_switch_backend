package kr.co.softhubglobal.controller.user;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.softhubglobal.dto.nicepay.NicepayDTO;
import kr.co.softhubglobal.entity.member.MemberOrder;
import kr.co.softhubglobal.entity.user.User;
import kr.co.softhubglobal.service.NicepayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

import static kr.co.softhubglobal.config.OpenApiConfig.BEARER_KEY_SECURITY_SCHEME;

@Controller
@RequestMapping("/user/api/v1/payments")
@Tag(name = "Nicepay payment", description = "NicePay payment APIs")
@RequiredArgsConstructor
public class NicepayController {

    private final NicepayService nicepayService;

    @PostMapping
    @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)
    public String checkout(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model,
            @RequestBody NicepayDTO.PaymentCreateRequest paymentCreateRequest
    ) {
        nicepayService.createPayment(((User) userDetails).getId(), model, paymentCreateRequest);
        return "index";
    }

    @PostMapping("/serverAuth")
    public String serverAuth(
            HttpServletRequest httpServletRequest,
            Model model
    ) {
        MemberOrder memberOrder = nicepayService.approvePayment(httpServletRequest);
        if(memberOrder == null) {
            model.addAttribute("resultCode", "ERROR");
            model.addAttribute("resultMsg", "ERROR");
            model.addAttribute("paymentStatus", 0);
            return "response";
        }
        model.addAttribute("resultCode", memberOrder.getResultCode());
        model.addAttribute("resultMsg", memberOrder.getResultMsg());
        model.addAttribute("paymentStatus", memberOrder.getId());
        return "response";
    }

    @PostMapping("/clientAuth")
    public String clientAuth(
            HttpServletRequest httpServletRequest,
            Model model
    ) {
        MemberOrder memberOrder = nicepayService.approvePaymentClientAuth(httpServletRequest);
        model.addAttribute("resultCode", memberOrder.getResultCode());
        model.addAttribute("resultMsg", memberOrder.getResultMsg());
        model.addAttribute("paymentStatus", memberOrder.getId());
        return "response";
    }


    @RequestMapping("/hook")
    public ResponseEntity<String> hook(
            @RequestBody HashMap<String, Object> hookMap
    ) throws Exception {
        String resultCode = hookMap.get("resultCode").toString();
        System.out.println("HERE ______ :" + hookMap);
        nicepayService.creditCardCallback(hookMap);
        if(resultCode.equalsIgnoreCase("0000")){
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
