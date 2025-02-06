package com.prod.main.baskettime.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prod.main.baskettime.entity.BasketballCourt;

public interface BasketballCourtRepository extends JpaRepository<BasketballCourt, Long> {
    Optional<BasketballCourt> findByPlaceName(String placeName); // 이름 중복 확인
}
