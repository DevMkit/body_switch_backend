package kr.co.softhubglobal.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import kr.co.softhubglobal.dto.AuthenticationDTO;
import kr.co.softhubglobal.entity.center.CenterManager;
import kr.co.softhubglobal.entity.center.CenterType;
import kr.co.softhubglobal.entity.user.Role;
import kr.co.softhubglobal.entity.user.User;
import kr.co.softhubglobal.exception.customExceptions.RequestNotAcceptableException;
import kr.co.softhubglobal.repository.CenterManagerRepository;
import kr.co.softhubglobal.security.TokenProvider;
import kr.co.softhubglobal.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final CenterManagerRepository centerManagerRepository;
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
        CenterType centerType = null;

//        if(user.getRole().equals(Role.MEMBER)) {
//            throw new BadCredentialsException("Bad credentials");
//        }

        if(user.getRole().equals(Role.MANAGER)){
            CenterManager centerManager = centerManagerRepository.findByUserUsername(user.getUsername())
                    .orElseThrow(() -> new BadCredentialsException("Bad credentials"));
            switch (centerManager.getStatus()) {
                case REFUSED -> throw new RequestNotAcceptableException("This account has already refused");
                case WITHDREW -> throw new RequestNotAcceptableException("This account has already withdrawn");
                case APPROVAL_PENDING -> throw new RequestNotAcceptableException("This account is waiting approve");
            }
            centerType = centerManager.getCenter().getCenterType();
        }

        String token = tokenProvider.generate(user.getUsername());
        Jws<Claims> decodedToken = tokenProvider.validateTokenAndGetJws(token)
                .orElseThrow(() -> new RuntimeException("Cannot generate token"));
        Date expiration = decodedToken.getBody().getExpiration();

        return new AuthenticationDTO.AuthenticationResponse(
                token,
                LocalDateTime.ofInstant(expiration.toInstant(), ZoneId.systemDefault()),
                "ROLE_" + user.getRole().toString(),
                centerType
        );
    }
}
