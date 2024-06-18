package kr.co.softhubglobal.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import kr.co.softhubglobal.dto.AuthenticationDTO;
import kr.co.softhubglobal.entity.user.Role;
import kr.co.softhubglobal.entity.user.User;
import kr.co.softhubglobal.security.TokenProvider;
import kr.co.softhubglobal.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final ObjectValidator<AuthenticationDTO.AuthenticationRequest> authenticationRequestObjectValidator;

    public AuthenticationDTO.AuthenticationResponse login(AuthenticationDTO.AuthenticationRequest authenticationRequest) {

        authenticationRequestObjectValidator.validate(authenticationRequest);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        User user = (User) authentication.getPrincipal();

        if(!user.getRole().equals(Role.MEMBER)) {
            throw new BadCredentialsException("Bad credentials");
        }

        String token = tokenProvider.generate(user.getUsername());
        Jws<Claims> decodedToken = tokenProvider.validateTokenAndGetJws(token)
                .orElseThrow(() -> new RuntimeException("Cannot generate token"));
        Date expiration = decodedToken.getBody().getExpiration();

        return new AuthenticationDTO.AuthenticationResponse(
                token,
                LocalDateTime.ofInstant(expiration.toInstant(), ZoneId.systemDefault())
        );
    }

    public AuthenticationDTO.AuthenticationResponse trainerLogin(AuthenticationDTO.AuthenticationRequest authenticationRequest) {

        authenticationRequestObjectValidator.validate(authenticationRequest);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        User user = (User) authentication.getPrincipal();

        if(!user.getRole().equals(Role.EMPLOYEE)) {
            throw new BadCredentialsException("Bad credentials");
        }

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
