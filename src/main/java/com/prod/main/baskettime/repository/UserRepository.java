package com.prod.main.baskettime.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import com.prod.main.baskettime.entity.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
}
