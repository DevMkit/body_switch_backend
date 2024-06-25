package kr.co.softhubglobal.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.softhubglobal.dto.nicepay.NicepayDTO;
import kr.co.softhubglobal.entity.course.CourseTicket;
import kr.co.softhubglobal.entity.member.Member;
import kr.co.softhubglobal.entity.member.MemberCourseTicket;
import kr.co.softhubglobal.entity.member.MemberCourseTicketStatus;
import kr.co.softhubglobal.entity.member.MemberOrder;
import kr.co.softhubglobal.exception.customExceptions.ResourceNotFoundException;
import kr.co.softhubglobal.repository.CourseTicketRepository;
import kr.co.softhubglobal.repository.MemberCourseTicketRepository;
import kr.co.softhubglobal.repository.MemberOrderRepository;
import kr.co.softhubglobal.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class NicepayService {

//    private final String CLIENT_ID = "S2_8dbbac5df51540bd8596baa0918079db";
//    private final String SECRET_KEY = "68769c63a816401ebcf73e2c6c20daa4";

    private final String CLIENT_ID = "S2_60f499cba3254923bb4e59134aab10b5";
    private final String SECRET_KEY = "50e14c509632411785747f9fd6ff174a";

    private final MemberRepository memberRepository;
    private final MemberOrderRepository memberOrderRepository;
    private final MemberCourseTicketRepository memberCourseTicketRepository;
    private final CourseTicketRepository courseTicketRepository;
    private final MessageSource messageSource;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void createPayment(
            Long userId,
            Model model,
            NicepayDTO.PaymentCreateRequest paymentCreateRequest
    ) {

        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("user.account.not.found", new Object[]{paymentCreateRequest.getTicketId()}, Locale.ENGLISH))
                );

        CourseTicket courseTicket = courseTicketRepository.findById(paymentCreateRequest.getTicketId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("course.ticket.id.not.exist", new Object[]{paymentCreateRequest.getTicketId()}, Locale.ENGLISH))
                );

        UUID orderId = UUID.randomUUID();

        memberOrderRepository.save(
                MemberOrder.builder()
                        .member(member)
                        .courseTicket(courseTicket)
                        .orderId(orderId.toString())
                        .paymentMethod("CARD")
                        .paymentStatus("ORDER_CREATED")
                        .courseStartDate(paymentCreateRequest.getCourseStartDate())
                        .build()
        );

        model.addAttribute("clientId", CLIENT_ID);
        model.addAttribute("orderId", orderId);
        model.addAttribute("amount", Math.round(courseTicket.getFinalPrice()));
        model.addAttribute("goodsName", courseTicket.getTicketName());
        model.addAttribute("returnUrl", "http://112.175.61.15:8082/user/api/v1/payments/serverAuth");
//        model.addAttribute("returnUrl", "http://localhost:8082/user/api/v1/payments/serverAuth");
//        model.addAttribute("returnUrl", "http://112.175.61.15:8082/user/api/v1/payments/clientAuth");
    }

    public MemberOrder approvePayment(
            HttpServletRequest httpServletRequest
    ) {
        try {
            Enumeration<String> params = httpServletRequest.getParameterNames();
            while(params.hasMoreElements()){
                String paramName = params.nextElement();
                System.out.println(" HERE " + paramName + " : "+ httpServletRequest.getParameter(paramName));
            }

            String authResultCode = httpServletRequest.getParameter("authResultCode");
            String authResultMsg = httpServletRequest.getParameter("authResultMsg");
            String tid = httpServletRequest.getParameter("tid");
            String clientId = httpServletRequest.getParameter("clientId");
            String orderId = httpServletRequest.getParameter("orderId");
            String amount = httpServletRequest.getParameter("amount");
            String authToken = httpServletRequest.getParameter("authToken");
            String signature = httpServletRequest.getParameter("signature");

            MemberOrder memberOrder = memberOrderRepository.findByOrderId(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            messageSource.getMessage("course.ticket.order.id.not.exist", new Object[]{orderId}, Locale.ENGLISH))
                    );
            memberOrder.setTid(tid);

            if(authResultCode.equalsIgnoreCase("0000")) {

                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((CLIENT_ID + ":" + SECRET_KEY).getBytes()));
                headers.setContentType(MediaType.APPLICATION_JSON);

                Map<String, Object> AuthenticationMap = new HashMap<>();
                AuthenticationMap.put("amount", amount);

                HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(AuthenticationMap), headers);

                ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                        "https://sandbox-api.nicepay.co.kr/v1/payments/" + tid,
                        request,
                        JsonNode.class
                );

                JsonNode responseNode = responseEntity.getBody();
                String resultCode = responseNode.get("resultCode").asText();

                System.out.println(responseNode.toPrettyString());

                if (resultCode.equalsIgnoreCase("0000")) {
                    System.out.println("Payment Success");
                    memberCourseTicketRepository.save(
                            MemberCourseTicket.builder()
                                    .member(memberOrder.getMember())
                                    .courseTicket(memberOrder.getCourseTicket())
                                    .startDate(memberOrder.getCourseStartDate())
                                    .expireDate(memberOrder.getCourseStartDate().plusMonths(memberOrder.getCourseTicket().getUsagePeriod()))
                                    .usedCount(0)
                                    .status(MemberCourseTicketStatus.ACTIVE)
                                    .build()
                    );
                } else {
                    System.out.println("Payment Failed");
                }

                memberOrder.setPaymentStatus(String.valueOf(responseNode.get("status").asText()).toUpperCase());
                memberOrder.setResultCode(resultCode);
                memberOrder.setResultMsg(responseNode.get("resultMsg").asText());

            } else {
                memberOrder.setPaymentStatus("CANCELED");
                memberOrder.setResultCode(authResultCode);
                memberOrder.setResultMsg(authResultMsg);
            }
            return memberOrderRepository.save(memberOrder);

        } catch (Exception e) {
            log.error("SERVER AUTH ERROR: {}" , e.getMessage());
            return null;
        }
    }

    public MemberOrder approvePaymentClientAuth(
            HttpServletRequest httpServletRequest
    ) {
        String resultCode = httpServletRequest.getParameter("resultCode");
        String resultMsg = httpServletRequest.getParameter("resultMsg");
        String tid = httpServletRequest.getParameter("tid");
        String orderId = httpServletRequest.getParameter("orderId");
        String paidAt = httpServletRequest.getParameter("paidAt");
        String status = httpServletRequest.getParameter("status");

        MemberOrder memberOrder = memberOrderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("course.ticket.order.id.not.exist", new Object[]{orderId}, Locale.ENGLISH))
                );

        memberOrder.setTid(tid);
        memberOrder.setResultCode(resultCode);
        memberOrder.setResultMsg(resultMsg);
        if(status != null) {
            memberOrder.setPaymentStatus(status);
        } else if(resultCode.equalsIgnoreCase("I002")) {
            memberOrder.setPaymentStatus("CANCELED");
        }
        if(paidAt != null) {
            memberOrder.setPaidAt(LocalDateTime.parse(paidAt));
        }
        return memberOrderRepository.save(memberOrder);
    }

    public void creditCardCallback(
            HashMap<String, Object> hookMap
    ) {
        String resultCode = hookMap.get("resultCode").toString();
        String resultMsg = hookMap.get("resultMsg").toString();
        String tid =  hookMap.get("tid").toString();
        String orderId = hookMap.get("orderId").toString();
        String paidAt = hookMap.get("paidAt").toString();
        String status = hookMap.get("status").toString();

        log.info("CALLBACK ORDER ID : {}", orderId);

        Optional<MemberOrder> optionalMemberOrder = memberOrderRepository.findByOrderId(orderId);
        if(optionalMemberOrder.isPresent()) {
            log.info("ORDER ID FOUND");
            MemberOrder memberOrder = optionalMemberOrder.get();
            memberOrder.setTid(tid);
            memberOrder.setResultCode(resultCode);
            memberOrder.setResultMsg(resultMsg);
            if(status != null) {
                memberOrder.setPaymentStatus(status);
            }
            if(paidAt != null) {
                memberOrder.setPaidAt(LocalDateTime.parse(paidAt));
            }
            memberOrderRepository.save(memberOrder);
        } else {
            log.info("UNKNOWN ORDER ID : {}", orderId);
        }
    }
}