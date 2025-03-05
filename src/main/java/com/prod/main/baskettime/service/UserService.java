package com.prod.main.baskettime.service;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.prod.main.baskettime.dto.GoogleLoginRequest;
import com.prod.main.baskettime.entity.Users;
import com.prod.main.baskettime.generator.NicknameGenerator;
import com.prod.main.baskettime.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadImageDir;

    @Value("${base.url}")
    private String baseUrl;

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
        // ✅ subId + type을 기반으로 유저 조회
        Users user = userRepository.findBySubIdAndType(request.getSubId(), request.getType()).orElse(null);

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

    // ✅ 30일 체크 로직
    public boolean canUpdateProfilePicture(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        LocalDateTime now = LocalDateTime.now();
        return user.getUpdatedAt() == null || user.getUpdatedAt().isBefore(now.minusDays(30));
    }

    // ✅ 프로필 이미지 변경 서비스
    public String updateProfilePicture(Long userId, MultipartFile file) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 30일 제한 체크
        if (!canUpdateProfilePicture(userId)) {
            throw new RuntimeException("프로필 이미지는 30일에 한 번만 변경할 수 있습니다.");
        }

        String imageUrl = saveImage(file); // 이미지 저장
        user.setPicture(imageUrl);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        return imageUrl;
    }

    // ✅ 이미지 저장 로직
    private String saveImage(MultipartFile file) {
        try {
            // 저장 경로 지정
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadImageDir + filename);

            // 디렉토리 없으면 생성
            Files.createDirectories(path.getParent());

            // 파일 저장
            Files.write(path, file.getBytes());

            return baseUrl + "/" +filename; // 저장된 파일명 반환
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }
}
