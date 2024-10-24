package com.prod.main.baskettime.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prod.main.baskettime.entity.Users;
import com.prod.main.baskettime.repository.UserRepository;

@RestController
public class LoginController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login/oauth2/google")
    public ResponseEntity<String> handleGoogleLogin(OAuth2AuthenticationToken authentication) {
        OAuth2User oauthUser = authentication.getPrincipal();
        String email = (String) oauthUser.getAttributes().get("email");
        String name = (String) oauthUser.getAttributes().get("name");
        String type = "G";

        Users user = userRepository.findByEmail(email)
            .orElse(new Users());
        
        // 필요에 따라 데이터베이스에 사용자 저장
        user.setEmail(email);
        user.setName(name);
        user.setType(type);
        user.setNickName("답도없는파괴왕987");
        userRepository.save(user);

        return ResponseEntity.ok("User authenticated and saved: " + name);
    }
}
