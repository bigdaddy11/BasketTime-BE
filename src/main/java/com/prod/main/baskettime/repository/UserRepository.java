package com.prod.main.baskettime.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import com.prod.main.baskettime.entity.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);

    List<Users> findByNickNameContainingIgnoreCase(String nickname);

    boolean existsByNickNameAndIdNot(String nickName, Long userId);

    Optional<Users> findBySubId(String googleId);
    Optional<Users> findBySubIdAndType(String subId, String type);

    Optional<Users> findByNickName(String nickname); 
}
