package com.prod.main.baskettime.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prod.main.baskettime.entity.Users;
import com.prod.main.baskettime.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 닉네임으로 유저 검색
     * @param query 닉네임 검색어
     * @return 닉네임이 포함된 유저 리스트
     */
    @GetMapping("/search")
    public ResponseEntity<List<Users>> searchUsers(@RequestParam(name = "query", required = false) String query) {
        List<Users> users = userService.searchByNickname(query);
        return ResponseEntity.ok(users);
    }

    /**
     * 유저 ID로 유저 상세 정보 조회
     * @param id 유저 ID
     * @return 유저 상세 정보
     */
    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        Users user = userService.findById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
