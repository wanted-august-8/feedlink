package com.feedlink.api.feedlink_api.domain.post.dto;

import com.feedlink.api.feedlink_api.domain.post.entity.Post;
import com.feedlink.api.feedlink_api.domain.post.enums.PostType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PostDTO {
    private Long postId;
    private String postTitle;
    private String postContent; // 최대 20자
    private Long postViewCnt;
    private Long postLikeCnt;
    private Long postShareCnt;
    private PostType postType;
    private LocalDateTime postCreateTime;
    private LocalDateTime postUpdateTime;
    private List<String> hashtags;

    public PostDTO(Post post) {
    }
}
