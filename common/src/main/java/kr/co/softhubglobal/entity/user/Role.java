package kr.co.softhubglobal.entity.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum Role {

    SUPERADMIN, OPERATOR, MANAGER, EMPLOYEE, MEMBER;

    public List<SimpleGrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.name()));
    }
}