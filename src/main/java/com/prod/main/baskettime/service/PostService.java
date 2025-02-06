package com.prod.main.baskettime.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prod.main.baskettime.entity.Post;
import com.prod.main.baskettime.entity.PostImages;
import com.prod.main.baskettime.repository.PostRepository;

import jakarta.transaction.Transactional;

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

    public Optional<Post> findById (Long id) {
        return postRepository.findById(id); // ID로 게시글 조회
    }

    public Page<Post> getPostsByCategoryId(Long categoryId, Long userId, String sort, Pageable pageable) {
        // Raw data 가져오기
        List<Object[]> rawPosts = postRepository.findPostsWithNickName(categoryId, userId, pageable.getOffset(), pageable.getPageSize(), sort);

        // Object[] 데이터를 Post 엔티티로 변환
        List<Post> posts = rawPosts.stream().map(row -> {
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
            post.setCommentCount(((Number) row[10]).longValue());
            post.setLikeCount(((Number) row[11]).longValue());
            post.setViewCount(((Number) row[12]).longValue());
            post.setIsLiked((Boolean) row[13]);
            post.setImageMainPath((String) row[14]);
            post.setImage((String) row[15]);
            return post;
        }).collect(Collectors.toList());

        // Total count 가져오기
        long total = postRepository.countPostsWithNickName(categoryId);

        // Page 객체 생성
        return new PageImpl<>(posts, pageable, total);
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
        
        if (row[11] != null) {
            String imageMainPathJson = (String) row[11];
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                List<String> imagePaths = objectMapper.readValue(imageMainPathJson, new TypeReference<List<String>>() {});
                List<PostImages> postImages = imagePaths.stream()
                                                        .map(PostImages::new)
                                                        .collect(Collectors.toList());
                post.setImagePaths(postImages);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse image paths", e);
            }
        } else {
            post.setImagePaths(Collections.emptyList());
        }
        return post;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // public Post createPost(Post post) {
    //     return postRepository.save(post);
    // }

    @Transactional
    public Post createPost(String title, String content, Long categoryId, Long userId, List<MultipartFile> images) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setCategoryId(categoryId);
        post.setUserId(userId);

        if (images != null && !images.isEmpty()) {
            List<PostImages> postImages = images.stream()
                    .map(image -> {
                        String imagePath = saveImage(image);
                        PostImages postImage = new PostImages();
                        postImage.setImagePath(imagePath);
                        postImage.setPost(post);
                        return postImage;
                    })
                    .collect(Collectors.toList());
            post.getImagePaths().addAll(postImages);
        }

        return postRepository.save(post);
    }

    // public Post updatePost(Long id, Post updatedPost) {
    //     return postRepository.findById(id)
    //             .map(post -> {
    //                 post.setCategoryId(updatedPost.getCategoryId());
    //                 post.setTitle(updatedPost.getTitle());
    //                 post.setContent(updatedPost.getContent());
    //                 return postRepository.save(post);
    //             })
    //             .orElseThrow(() -> new RuntimeException("Post not found with id " + id));
    // }
    @Transactional
    public Post updatePost(Long id, String title, String content, Long categoryId, List<MultipartFile> images) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.getImagePaths().clear(); // 기존 이미지 삭제

        if (images != null && !images.isEmpty()) {
            List<PostImages> postImages = images.stream()
                    .map(image -> {
                        String imagePath = saveImage(image);
                        PostImages postImage = new PostImages();
                        postImage.setImagePath(imagePath);
                        postImage.setPost(post);
                        return postImage;
                    })
                    .collect(Collectors.toList());
            post.getImagePaths().addAll(postImages);
        }

        post.setTitle(title);
        post.setContent(content);
        post.setCategoryId(categoryId);

        return postRepository.save(post);
    }
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    private String saveImage(MultipartFile file) {
        try {
            // 고정된 업로드 디렉토리 설정 (예: C:/uploads/)
            String uploadDir = "C:/uploads/";
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + filename);

            // 디렉토리 없으면 생성
            Files.createDirectories(path.getParent());

            // 파일 저장
            Files.write(path, file.getBytes());

            // URL 경로 반환
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    // 이미지 삭제 메서드
    private void deleteImage(String filename) {
        try {
            String uploadDir = "C:/uploads/";
            Path path = Paths.get(uploadDir + filename);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            System.err.println("Failed to delete image: " + filename);
        }
    }
}
