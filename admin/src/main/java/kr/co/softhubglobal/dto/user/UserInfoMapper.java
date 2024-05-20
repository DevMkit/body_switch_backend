package kr.co.softhubglobal.dto.user;

import kr.co.softhubglobal.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserInfoMapper implements Function<User, UserDTO.UserInfo> {

    @Override
    public UserDTO.UserInfo apply(User user) {
        return new UserDTO.UserInfo(
                user.getUsername(),
                user.getName(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getUserType().toString()
        );
    }
}
