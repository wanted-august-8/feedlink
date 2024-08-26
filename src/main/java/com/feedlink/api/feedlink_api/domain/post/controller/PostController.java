package com.feedlink.api.feedlink_api.domain.post.controller;

import com.feedlink.api.feedlink_api.domain.post.dto.PostDTO;
import com.feedlink.api.feedlink_api.domain.post.entity.Post;
import com.feedlink.api.feedlink_api.domain.post.enums.PostType;
import com.feedlink.api.feedlink_api.domain.post.service.PostService;
import com.feedlink.api.feedlink_api.domain.security.PrincipalDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
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
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestParam(required = false) String hashtag,
        @RequestParam(required = false) PostType type,
        @RequestParam(defaultValue = "created_at") String orderBy,
        @RequestParam(defaultValue = "title,content") String searchBy,
        @RequestParam(required = false) String search,
        @RequestParam(defaultValue = "10") int pageCount,
        @RequestParam(defaultValue = "0") int page
    ) {
        String memberAccount = principalDetails.getMember().getMemberAccount();
        Page<PostDTO> posts = postService.getAllPosts(memberAccount, hashtag, type, orderBy, searchBy, search, pageCount, page);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
        PostDTO post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }
}