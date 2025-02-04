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
import com.prod.main.baskettime.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class LoginController {
    @Autowired
    private UserRepository userRepository
    ;
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Users> googleLogin(@RequestBody GoogleLoginRequest request) {
        try {
            // 사용자 조회 또는 등록
            Users user = userService.findOrCreateUser(request);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{userId}/nickname")
    public ResponseEntity<?> updateNickname(
        @PathVariable(name = "userId", required = false) Long userId,
        @RequestParam(name = "nickname", required = false) String nickname) {
        Optional<Users> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            
            // 이미 수정된 닉네임인지 확인
            if (user.isEditIs()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("닉네임은 이미 수정되었습니다.");
            }

            // 닉네임 업데이트
            user.setNickName(nickname);
            user.setEditIs(true); // 수정 플래그 true로 변경
            userRepository.save(user);

            return ResponseEntity.ok("닉네임이 성공적으로 변경되었습니다.");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
    }
}

