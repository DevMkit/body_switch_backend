package kr.co.softhubglobal.repository;

import kr.co.softhubglobal.entity.user.Role;
import kr.co.softhubglobal.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndRoleNotIn(String username, List<Role> roleList);

    boolean existsByUsernameAndRoleIn(String username, List<Role> roleList);
}
