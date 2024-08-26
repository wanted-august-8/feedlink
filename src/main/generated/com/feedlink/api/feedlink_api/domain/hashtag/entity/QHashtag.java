package com.feedlink.api.feedlink_api.domain.hashtag.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHashtag is a Querydsl query type for Hashtag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHashtag extends EntityPathBase<Hashtag> {

    private static final long serialVersionUID = 615213999L;

    public static final QHashtag hashtag = new QHashtag("hashtag");

    public final NumberPath<Long> hashtagId = createNumber("hashtagId", Long.class);

    public final StringPath hashtagName = createString("hashtagName");

    public final ListPath<com.feedlink.api.feedlink_api.domain.posthashtag.entity.PostHashtag, com.feedlink.api.feedlink_api.domain.posthashtag.entity.QPostHashtag> postHashtagList = this.<com.feedlink.api.feedlink_api.domain.posthashtag.entity.PostHashtag, com.feedlink.api.feedlink_api.domain.posthashtag.entity.QPostHashtag>createList("postHashtagList", com.feedlink.api.feedlink_api.domain.posthashtag.entity.PostHashtag.class, com.feedlink.api.feedlink_api.domain.posthashtag.entity.QPostHashtag.class, PathInits.DIRECT2);

    public QHashtag(String variable) {
        super(Hashtag.class, forVariable(variable));
    }

    public QHashtag(Path<? extends Hashtag> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHashtag(PathMetadata metadata) {
        super(Hashtag.class, metadata);
    }

}

