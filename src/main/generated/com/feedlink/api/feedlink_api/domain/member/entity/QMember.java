package com.feedlink.api.feedlink_api.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1878063741L;

    public static final QMember member = new QMember("member1");

    public final StringPath memberAccount = createString("memberAccount");

    public final StringPath memberCode = createString("memberCode");

    public final StringPath memberEmail = createString("memberEmail");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath memberPwd = createString("memberPwd");

    public final BooleanPath memberStatus = createBoolean("memberStatus");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

