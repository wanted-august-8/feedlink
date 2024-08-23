package com.feedlink.api.feedlink_api.domain.post.entity;

import com.feedlink.api.feedlink_api.domain.hashtag.entity.Hashtag;
import com.feedlink.api.feedlink_api.domain.posthashtag.entity.PostHashtag;
import com.feedlink.api.feedlink_api.domain.post.enums.PostType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="post")
@Getter
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    private String postTitle;
    private String postContent;
    private Long postViewCnt;
    private Long postLikeCnt;
    private Long postShareCnt;
    @Enumerated(EnumType.STRING)
    private PostType postType;
    private LocalDateTime postCreateTime;
    private LocalDateTime postUpdateTime;
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostHashtag> postHashtagList = new ArrayList<>();

    @Builder
    public Post(Long postId, String postTitle, String postContent, Long postViewCnt,
        Long postLikeCnt,
        Long postShareCnt, PostType postType, LocalDateTime postCreateTime,
        LocalDateTime postUpdateTime) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postViewCnt = postViewCnt;
        this.postLikeCnt = postLikeCnt;
        this.postShareCnt = postShareCnt;
        this.postType = postType;
        this.postCreateTime = postCreateTime;
        this.postUpdateTime = postUpdateTime;
    }

    public void addHashtag(Hashtag hashtag) {
        PostHashtag postHashtag = PostHashtag.builder().post(this).hashtag(hashtag).build();
        this.postHashtagList.add(postHashtag);
    }
}
