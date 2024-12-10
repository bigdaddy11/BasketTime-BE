package com.prod.main.baskettime.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prod.main.baskettime.entity.Post;
import com.prod.main.baskettime.entity.PostComment;
import com.prod.main.baskettime.repository.PostCommentRepository;

@Service
public class PostCommentService {
    private final PostCommentRepository postCommentRepository;

    public PostCommentService(PostCommentRepository postCommentRepository) {
        this.postCommentRepository = postCommentRepository;
    }

    public List<PostComment> getPostCommentsByPostId(Long postId) {
        List<Object[]> rawPosts = postCommentRepository.findPostCommentWithNickName(postId);

        // Object[] 데이터를 Post 엔티티로 변환
        List<PostComment> postComments = new ArrayList<>();
        for (Object[] row : rawPosts) {
            PostComment postComment = new PostComment();
            postComment.setId(((Number) row[0]).longValue());
            postComment.setCommentText((String) row[1]);
            postComment.setNickName((String) row[2]);
            postComment.setTimeAgo((String) row[3]);

            postComments.add(postComment);
        }
        return postComments;
    }
    // 특정 게시글의 댓글 가져오기
    public List<PostComment> getCommentsByPostId(Long postId) {
        return postCommentRepository.findByPostIdOrderByCreatedAtAsc(postId);
    }

    // 댓글 작성
    @Transactional
    public PostComment addComment(PostComment comment) {
        return postCommentRepository.save(comment);
    }
}
