package com.prod.main.baskettime.service;

import com.prod.main.baskettime.entity.PlayerComment;
import com.prod.main.baskettime.repository.PlayerCommentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerCommentService {

    @Autowired
    private PlayerCommentRepository playerCommentRepository;

    // 특정 선수의 댓글 목록을 불러오는 메서드
    public List<PlayerComment> getCommentsByPlayerIdAndType(Long playerId, String type) {
        return playerCommentRepository.findByPlayerIdAndType(playerId, type, Sort.by(Sort.Direction.ASC, "createdAt"));
    }
}
