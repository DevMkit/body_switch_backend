package kr.co.softhubglobal.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import kr.co.softhubglobal.dto.AuthenticationDTO;
import kr.co.softhubglobal.entity.store.StoreRepresentative;
import kr.co.softhubglobal.entity.user.User;
import kr.co.softhubglobal.entity.user.UserType;
import kr.co.softhubglobal.exception.customExceptions.RequestNotAcceptableException;
import kr.co.softhubglobal.repository.StoreRepresentativeRepository;
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

    private final StoreRepresentativeRepository storeRepresentativeRepository;
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

        if(user.getUserType().equals(UserType.MEMBER)) {
            throw new BadCredentialsException("Bad credentials");
        }

        if(user.getUserType().equals(UserType.STORE_REPRESENTATIVE)){
            StoreRepresentative storeRepresentative = storeRepresentativeRepository.findByUserUsername(user.getUsername())
                    .orElseThrow(() -> new BadCredentialsException("Bad credentials"));
            switch (storeRepresentative.getStore().getStatus()) {
                case REFUSED -> throw new RequestNotAcceptableException("This account has already refused");
                case WITHDREW -> throw new RequestNotAcceptableException("This account has already withdrawn");
                case APPROVAL_PENDING -> throw new RequestNotAcceptableException("This account is waiting approve");
            }
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
