package kr.co.softhubglobal.repository;

import kr.co.softhubglobal.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
