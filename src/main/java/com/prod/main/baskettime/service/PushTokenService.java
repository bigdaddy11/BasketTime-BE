package com.prod.main.baskettime.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prod.main.baskettime.entity.PushToken;
import com.prod.main.baskettime.entity.Users;
import com.prod.main.baskettime.repository.PushTokenRepository;
import com.prod.main.baskettime.repository.UserRepository;

@Service
public class PushTokenService {
    private final PushTokenRepository pushTokenRepository;
    private final UserRepository userRepository;

    public PushTokenService(PushTokenRepository pushTokenRepository, UserRepository userRepository) {
        this.pushTokenRepository = pushTokenRepository;
        this.userRepository = userRepository;
    }

    /**
     * ✅ 푸시 토큰 저장 (기존 토큰이 있으면 업데이트, 없으면 새로 추가)
     */
    @Transactional
    public void saveOrUpdatePushToken(Long userId, String pushToken, String deviceType) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ 기존 토큰 조회
        PushToken existingToken = pushTokenRepository.findByUserIdAndDeviceType(userId, deviceType).orElse(null);

        if (existingToken != null) {
            // ✅ 기존 토큰이 변경된 경우 날짜 및 토큰 업데이트 (최신 로그인 여부 업데이트 날짜로 확인)
            existingToken.setToken(pushToken);
            existingToken.setUpdatedAt(LocalDateTime.now());
            pushTokenRepository.save(existingToken);
            
        } else {
            // ✅ 기존 토큰이 없으면 새로 추가
            PushToken newToken = new PushToken(user, pushToken, deviceType);
            pushTokenRepository.save(newToken);
        }
    }
}
