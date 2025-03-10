package com.prod.main.baskettime.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.prod.main.baskettime.entity.Post;
import com.prod.main.baskettime.repository.PostRepository;
import com.prod.main.baskettime.service.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 카테고리별 조회
    @GetMapping
    public ResponseEntity<Page<Post>> getPostsByCategory(
        @RequestParam(name = "categoryId", required = false) Long categoryId,
        @RequestParam(name = "userId", required = false) Long userId,
        @RequestParam(name = "sort", required = false) String sort,
        Pageable pageable) {
        Page<Post> posts = postService.getPostsByCategoryId(categoryId, userId, sort, pageable);
        return ResponseEntity.ok(posts);
    }

    // // 카테고리별 조회
    // @GetMapping("/detail")
    // public ResponseEntity<List<Post>> getPostsWithId(@RequestParam(name = "id", required = false) Long id) {
    //     List<Post> posts = postService.getPostsWithId(id);
    //     return ResponseEntity.ok(posts);
    // }

    // 게시글 ID로 조회
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable("id") Long id) {
        // Post post = postService.getPostById(id); // PostService 호출\
        System.out.println(id);
        Post post = postService.findPostsWithId(id); // PostService 호출
        if (post != null) {
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.notFound().build(); // 게시글이 없을 경우 404 반환
        }
    }

    // 게시글 생성
    // @PostMapping
    // public ResponseEntity<Post> createPost(@RequestBody Post post) {
    //     return ResponseEntity.ok(postService.createPost(post));
    // }

     // 게시글 생성 (이미지 포함)
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Post> createPost(
        @RequestParam("title") String title,
        @RequestParam("content") String content,
        @RequestParam("categoryId") Long categoryId,
        @RequestParam("userId") Long userId,
        @RequestParam(value = "images", required = false) List<MultipartFile> images) {
            Post post = postService.createPost(title, content, categoryId, userId, images);
            return ResponseEntity.ok(post);
    }

    // 게시글 수정
    // @PutMapping("/{id}")
    // public ResponseEntity<Post> updatePost(@PathVariable("id") Long id, @RequestBody Post updatedPost) {
    //     return ResponseEntity.ok(postService.updatePost(id, updatedPost));
    // }
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
        public ResponseEntity<Post> updatePost(
        @PathVariable("id") Long id,
        @RequestParam("title") String title,
        @RequestParam("content") String content,
        @RequestParam("categoryId") Long categoryId,
        @RequestParam(value = "images", required = false) List<MultipartFile> images) {
            Post post = postService.updatePost(id, title, content, categoryId, images);
            return ResponseEntity.ok(post);
        }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

}
