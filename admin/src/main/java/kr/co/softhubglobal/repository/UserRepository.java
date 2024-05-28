package kr.co.softhubglobal.repository;

import kr.co.softhubglobal.entity.user.Role;
import kr.co.softhubglobal.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsernameAndRoleNotLike(String username, Role role);

    boolean existsByUsernameAndRoleIn(String username, List<Role> roleList);
}
