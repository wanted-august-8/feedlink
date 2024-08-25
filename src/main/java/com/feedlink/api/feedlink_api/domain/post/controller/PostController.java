package com.feedlink.api.feedlink_api.domain.post.controller;

import com.feedlink.api.feedlink_api.domain.post.dto.PostDTO;
import com.feedlink.api.feedlink_api.domain.post.entity.Post;
import com.feedlink.api.feedlink_api.domain.post.enums.PostType;
import com.feedlink.api.feedlink_api.domain.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<Page<PostDTO>> getAllPosts(
        @RequestParam(required = false) String hashtag,
        @RequestParam(required = false) PostType type,
        @RequestParam(defaultValue = "postCreateTime") String orderBy,
        @RequestParam(defaultValue = "title,content") String searchBy,
        @RequestParam(required = false) String search,
        @RequestParam(defaultValue = "10") int pageCount,
        @RequestParam(defaultValue = "0") int page
    ) {
        Page<PostDTO> posts = postService.getAllPosts(hashtag, type, orderBy, searchBy, search, pageCount, page);
        return ResponseEntity.ok(posts);
    }
}
