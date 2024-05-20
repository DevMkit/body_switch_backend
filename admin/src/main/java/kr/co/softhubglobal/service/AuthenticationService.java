package kr.co.softhubglobal.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import kr.co.softhubglobal.dto.AuthenticationDTO;
import kr.co.softhubglobal.entity.user.User;
import kr.co.softhubglobal.repository.UserRepository;
import kr.co.softhubglobal.security.TokenProvider;
import kr.co.softhubglobal.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final MessageSource messageSource;
    private final ObjectValidator<AuthenticationDTO.AuthenticationRequest> authenticationRequestObjectValidator;

    public AuthenticationDTO.AuthenticationResponse login(AuthenticationDTO.AuthenticationRequest authenticationRequest) {

        authenticationRequestObjectValidator.validate(authenticationRequest);

//        User user = userRepository.findById(authenticationRequest.getUsername())
//                .orElseThrow(() -> new ResourceNotFoundException(
//                        messageSource.getMessage("member.email.not.exist", new Object[]{authenticationRequest.getUsername()}, Locale.ENGLISH)));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        User user = (User) authentication.getPrincipal();

        String token = tokenProvider.generate(user.getUsername());
        Jws<Claims> decodedToken = tokenProvider.validateTokenAndGetJws(token)
                .orElseThrow(() -> new RuntimeException("Cannot generate token"));
        Date expiration = decodedToken.getBody().getExpiration();

        return new AuthenticationDTO.AuthenticationResponse(
                token,
                LocalDateTime.ofInstant(expiration.toInstant(), ZoneId.systemDefault())
        );
    }
}
