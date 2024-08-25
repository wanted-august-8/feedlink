package com.feedlink.api.feedlink_api.domain.post.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = 1267963209L;

    public static final QPost post = new QPost("post");

    public final StringPath postContent = createString("postContent");

    public final DateTimePath<java.time.LocalDateTime> postCreateTime = createDateTime("postCreateTime", java.time.LocalDateTime.class);

    public final ListPath<com.feedlink.api.feedlink_api.domain.posthashtag.entity.PostHashtag, com.feedlink.api.feedlink_api.domain.posthashtag.entity.QPostHashtag> postHashtagList = this.<com.feedlink.api.feedlink_api.domain.posthashtag.entity.PostHashtag, com.feedlink.api.feedlink_api.domain.posthashtag.entity.QPostHashtag>createList("postHashtagList", com.feedlink.api.feedlink_api.domain.posthashtag.entity.PostHashtag.class, com.feedlink.api.feedlink_api.domain.posthashtag.entity.QPostHashtag.class, PathInits.DIRECT2);

    public final NumberPath<Long> postId = createNumber("postId", Long.class);

    public final NumberPath<Long> postLikeCnt = createNumber("postLikeCnt", Long.class);

    public final NumberPath<Long> postShareCnt = createNumber("postShareCnt", Long.class);

    public final StringPath postTitle = createString("postTitle");

    public final EnumPath<com.feedlink.api.feedlink_api.domain.post.enums.PostType> postType = createEnum("postType", com.feedlink.api.feedlink_api.domain.post.enums.PostType.class);

    public final DateTimePath<java.time.LocalDateTime> postUpdateTime = createDateTime("postUpdateTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> postViewCnt = createNumber("postViewCnt", Long.class);

    public QPost(String variable) {
        super(Post.class, forVariable(variable));
    }

    public QPost(Path<? extends Post> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPost(PathMetadata metadata) {
        super(Post.class, metadata);
    }

}

