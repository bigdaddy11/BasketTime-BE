package com.prod.main.baskettime.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prod.main.baskettime.dto.GoogleLoginRequest;
import com.prod.main.baskettime.entity.Users;
import com.prod.main.baskettime.repository.UserRepository;
import com.prod.main.baskettime.service.PushTokenService;
import com.prod.main.baskettime.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class LoginController {
    @Autowired
    private UserRepository userRepository;

    private final UserService userService;
    private final PushTokenService pushTokenService;

    public LoginController(UserService userService, PushTokenService pushTokenService) {
        this.userService = userService;
        this.pushTokenService = pushTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<Users> googleLogin(
        @RequestBody GoogleLoginRequest request,
        @RequestParam(required = false) String pushToken,
        @RequestParam(required = false) String deviceType) {
        try {
            // 사용자 조회 또는 등록
            Users user = userService.findOrCreateUser(request, pushToken, deviceType);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ✅ 앱 실행 시 푸시 토큰 업데이트 (재설치 대비)
     */
    @PostMapping("/update-push-token")
    public void updatePushToken(@RequestParam Long userId,
                                @RequestParam String pushToken,
                                @RequestParam String deviceType) {
        pushTokenService.saveOrUpdatePushToken(userId, pushToken, deviceType);
    }

    @PutMapping("/{userId}/nickname")
    public ResponseEntity<?> updateNickname(
        @PathVariable(name = "userId") Long userId,
        @RequestParam(name = "nickname") String nickname) {
        
        Optional<Users> optionalUser = userRepository.findById(userId);
        
        // 닉네임 길이 제한 
        if (nickname.length() > 8) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("닉네임은 최대 8자까지만 가능합니다.");
        }

        // 닉네임 값이 null이거나 비어있는 경우 예외처리
        if (nickname == null || nickname.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("닉네임을 입력해주세요.");
        }

        // 사용자가 존재하지 않을 경우 404 반환
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }

        Users user = optionalUser.get();

        // 이미 닉네임을 변경한 경우 400 반환
        if (user.isEditIs()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("닉네임은 이미 수정되었습니다.");
        }

        // 중복 닉네임 검사 (다른 사용자가 동일한 닉네임을 사용하고 있는지 체크)
        boolean isNicknameTaken = userRepository.existsByNickNameAndIdNot(nickname, userId);
        if (isNicknameTaken) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 닉네임입니다.");
        }

        // ✅ 닉네임 업데이트 중
        user.setNickName(nickname);
        user.setEditIs(true);
        userRepository.save(user);

        return ResponseEntity.ok("닉네임이 성공적으로 변경되었습니다.");
    }
}

