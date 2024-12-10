package com.prod.main.baskettime.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.prod.main.baskettime.entity.Post;
import com.prod.main.baskettime.repository.PostRepository;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // 게시글 ID로 조회
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null); // ID로 게시글 조회
    }

    public List<Post> getPostsByCategoryId(Long categoryId) {
        List<Object[]> rawPosts = postRepository.findPostsWithNickName(categoryId);

        // Object[] 데이터를 Post 엔티티로 변환
        List<Post> posts = new ArrayList<>();
        for (Object[] row : rawPosts) {
            Post post = new Post();
            post.setId(((Number) row[0]).longValue());
            post.setCategoryId(((Number) row[1]).longValue());
            post.setContent((String) row[2]);
            post.setCreatedAt(row[3] != null ? ((java.sql.Timestamp) row[3]).toLocalDateTime() : null);
            post.setTitle((String) row[4]);
            post.setUpdatedAt(row[5] != null ? ((java.sql.Timestamp) row[5]).toLocalDateTime() : null);
            post.setUserId(((Number) row[6]).longValue());
            post.setNickName((String) row[7]);
            post.setCategoryName((String) row[8]);
            post.setTimeAgo((String) row[9]);
            posts.add(post);
        }
        return posts;
    }

    public Post findPostsWithId(Long id) {
        List<Object[]> result = postRepository.findPostsWithId(id);
        if (result == null || result.isEmpty()) {
            throw new RuntimeException("Post not found with id: " + id);
        }
        Object[] row = result.get(0);
        return mapToPost(row);
    }

    private Post mapToPost(Object[] row) {
        Post post = new Post();
        // 각 Object[]의 인덱스에 맞는 데이터 타입으로 변환
        post.setContent((String) row[2]); // String
        post.setId(((Number) row[0]).longValue()); // Long (숫자 변환)
        post.setCategoryId(((Long) row[1])); // Long
        post.setCreatedAt(row[3] != null ? ((java.sql.Timestamp) row[3]).toLocalDateTime() : null);
        post.setTitle((String) row[4]); // String
        post.setUpdatedAt(row[5] != null ? ((java.sql.Timestamp) row[5]).toLocalDateTime() : null);
        post.setUserId(((Long) row[6])); // Long
        post.setNickName((String) row[7]); // String
        post.setCategoryName((String) row[8]); // String
        post.setTimeAgo((String) row[9]); // String
        post.setImage((String) row[10]); // String
        return post;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Post updatePost(Long id, Post updatedPost) {
        return postRepository.findById(id)
                .map(post -> {
                    post.setCategoryId(updatedPost.getCategoryId());
                    post.setTitle(updatedPost.getTitle());
                    post.setContent(updatedPost.getContent());
                    return postRepository.save(post);
                })
                .orElseThrow(() -> new RuntimeException("Post not found with id " + id));
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
