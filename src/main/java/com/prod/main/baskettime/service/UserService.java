package com.prod.main.baskettime.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prod.main.baskettime.dto.GoogleLoginRequest;
import com.prod.main.baskettime.entity.Users;
import com.prod.main.baskettime.generator.NicknameGenerator;
import com.prod.main.baskettime.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    /**
     * 닉네임으로 유저 검색
     * @param query 닉네임 검색어
     * @return 닉네임이 포함된 유저 리스트
     */
    public List<Users> searchByNickname(String query) {
        return userRepository.findByNickNameContainingIgnoreCase(query);
    }

    /**
     * 유저 ID로 유저 정보 조회
     * @param id 유저 ID
     * @return 유저 정보
     */
    public Users findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public Users findOrCreateUser(GoogleLoginRequest request) {
        // 이메일 또는 Google ID로 사용자 찾기
        Users user = userRepository.findBySubId(request.getSubId())
                .orElseGet(() -> userRepository.findByEmail(request.getEmail()).orElse(null));

        if (user == null) {
            String randomNickname = NicknameGenerator.generateRandomNickname();
            // 사용자 등록
            user = new Users();
            user.setSubId(request.getSubId());
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPicture(request.getPicture());
            user.setType(request.getType());
            user.setNickName(randomNickname);
            user.setEditIs(false); // 닉네임 수정 여부 초기화
            user = userRepository.save(user);
        } else {
            // 기존 사용자 정보와 비교하여 변경사항 확인 및 업데이트
            boolean isUpdated = false;
    
            if (!user.getName().equals(request.getName())) {
                user.setName(request.getName());
                isUpdated = true;
            }
            if (!user.getEmail().equals(request.getEmail())) {
                user.setEmail(request.getEmail());
                isUpdated = true;
            }
            if (!user.getPicture().equals(request.getPicture())) {
                user.setPicture(request.getPicture());
                isUpdated = true;
            }
    
            // 필요한 경우 다른 필드도 비교 및 업데이트
            if (isUpdated) {
                user = userRepository.save(user); // 변경사항 저장
            }
        }

        return user;
    }
}
