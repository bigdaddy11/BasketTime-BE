package com.prod.main.baskettime.service;

import com.prod.main.baskettime.entity.PlayerComment;
import com.prod.main.baskettime.entity.Post;
import com.prod.main.baskettime.repository.PlayerCommentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerCommentService {

    @Autowired
    private PlayerCommentRepository playerCommentRepository;

    // 특정 선수의 댓글 목록을 불러오는 메서드
    // public List<PlayerComment> getCommentsByPlayerIdAndType(Long playerId, String type) {
    //     return playerCommentRepository.findByPlayerIdAndType(playerId, type, Sort.by(Sort.Direction.ASC, "createdAt"));
    // }

    public List<PlayerComment> getCommentsByPlayerIdAndType(Long playerId, String type) {
        List<Object[]> result = playerCommentRepository.findPlayerWithIdAndType(playerId, type);

        // Object[] 데이터를 Post 엔티티로 변환

        List<PlayerComment> posts = new ArrayList<>();
        for (Object[] row : result) {
            PlayerComment post = new PlayerComment();
            // 각 Object[]의 인덱스에 맞는 데이터 타입으로 변환
            post.setId(((Number) row[0]).longValue()); // Long (숫자 변환)
            post.setCommentText(((String) row[1])); // Long
            post.setPlayerId(((Number) row[2]).longValue());
            post.setType(((String) row[3]));
            post.setUserId(((Number) row[4]).longValue());
            post.setNickName(((String) row[5]));
            post.setTimeAgo(((String) row[6]));
            posts.add(post);
        }
        return posts;
    }
}
