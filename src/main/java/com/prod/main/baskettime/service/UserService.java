package com.prod.main.baskettime.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prod.main.baskettime.entity.Users;
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
}
