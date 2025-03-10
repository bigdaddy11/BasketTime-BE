package com.prod.main.baskettime.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prod.main.baskettime.entity.PushToken;
import com.prod.main.baskettime.entity.Users;

public interface PushTokenRepository extends JpaRepository<PushToken, Long> {
    
    // 특정 사용자 ID로 모든 토큰 조회
    List<PushToken> findByUserId(Long userId);

    // 특정 사용자 ID와 디바이스 타입(android, ios)으로 토큰 조회
    Optional<PushToken> findByUserIdAndDeviceType(Long userId, String deviceType);

    // 특정 토큰 삭제
    void deleteByToken(String token);

    // 특정 사용자 ID + 디바이스 타입의 기존 토큰 삭제
    void deleteByUserIdAndDeviceType(Long userId, String deviceType);

    List<PushToken> findByUser(Users user);
}
