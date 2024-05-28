package kr.co.softhubglobal.service;

import kr.co.softhubglobal.dto.user.UserDTO;
import kr.co.softhubglobal.dto.user.UserInfoMapper;
import kr.co.softhubglobal.entity.user.Role;
import kr.co.softhubglobal.entity.user.User;
import kr.co.softhubglobal.exception.customExceptions.DuplicateResourceException;
import kr.co.softhubglobal.repository.UserRepository;
import kr.co.softhubglobal.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserInfoMapper userInfoMapper;
    private final MessageSource messageSource;
    private final ObjectValidator<UserDTO.UserCreateRequest> userCreateRequestObjectValidator;
    private final PasswordEncoder passwordEncoder;

    public List<UserDTO.UserInfo> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userInfoMapper)
                .toList();
    }

    public void createUserRecord(UserDTO.UserCreateRequest userCreateRequest) {
        userCreateRequestObjectValidator.validate(userCreateRequest);

        if(userRepository.existsById(userCreateRequest.getUsername())) {
            throw new DuplicateResourceException(
                    messageSource.getMessage("user.username.already.exists", new Object[]{userCreateRequest.getUsername()}, Locale.ENGLISH));
        }

        userRepository.save(
                User.builder()
                        .username(userCreateRequest.getUsername())
                        .name(userCreateRequest.getName())
                        .phoneNumber(userCreateRequest.getPhoneNumber())
                        .email(userCreateRequest.getEmail())
                        .password(passwordEncoder.encode(userCreateRequest.getPassword()))
                        .role(Role.OPERATOR)
                        .build()
        );
    }
}
