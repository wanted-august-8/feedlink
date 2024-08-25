package com.feedlink.api.feedlink_api.domain.statistic.repository;

import com.feedlink.api.feedlink_api.domain.hashtag.entity.QHashtag;
import com.feedlink.api.feedlink_api.domain.post.entity.QPost;
import com.feedlink.api.feedlink_api.domain.posthashtag.entity.QPostHashtag;
import com.feedlink.api.feedlink_api.domain.statistic.dto.StatisticCondition;
import com.feedlink.api.feedlink_api.domain.statistic.request.StatisticReqDTO;
import com.feedlink.api.feedlink_api.domain.statistic.dto.StatisticResultDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class StatisticRepositoryImpl implements StatisticRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    /**
     * 특정 해시태그와 날짜, target, hashtag, byHour에 따른 게시물 통계 정보를 조회합니다.
     *
     * @param condition
     *
     * @return 주어진 조건에 맞는 게시물 통계 데이터를 담은 StatisticDTO 객체의 리스트를 반환합니다.
     */
    public List<StatisticResultDTO> getPostStatistics(StatisticCondition condition) {
        QPost post = QPost.post;
        QPostHashtag postHashtag = QPostHashtag.postHashtag;
        QHashtag hashtag = QHashtag.hashtag;

        // 날짜 포맷 결정
        StringTemplate dateFormat = condition.getByHour()
            ? Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:00')", post.postCreateTime)
            : Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", post.postCreateTime);

        // 서브쿼리 작성
        JPAQuery<Long> subQuery = queryFactory
            .select(postHashtag.post.postId)
            .from(postHashtag)
            .innerJoin(hashtag)
            .on(postHashtag.hashtag.hashtagId.eq(hashtag.hashtagId))
            .where(hashtag.hashtagName.eq(condition.getHashtag()));

        // 날짜 필터 조건
        BooleanExpression dateCondition = post.postCreateTime.between(condition.getStartDateTime(), condition.getEndDateTime());

        // Projections 설정
        QBean<StatisticResultDTO> projection;

        switch (condition.getTarget()) {
            case "view_count":
                projection = Projections.bean(
                    StatisticResultDTO.class,
                    dateFormat.as("statisticKey"),
                    post.postViewCnt.sum().as("viewCnt")
                );
                break;
            case "like_count":
                projection = Projections.bean(
                    StatisticResultDTO.class,
                    dateFormat.as("statisticKey"),
                    post.postLikeCnt.sum().as("likeCnt")
                );
                break;
            case "share_count":
                projection = Projections.bean(
                    StatisticResultDTO.class,
                    dateFormat.as("statisticKey"),
                    post.postShareCnt.sum().as("shareCnt")
                );
                break;
            default:
                projection = Projections.bean(
                    StatisticResultDTO.class,
                    dateFormat.as("statisticKey"),
                    post.postId.count().as("postCnt")
                );
        }

        // 메인 쿼리
        return queryFactory
            .select(projection)
            .from(post)
            .where(post.postId.in(subQuery))
            .where(dateCondition)
            .groupBy(dateFormat)
            .fetch();
    }

}
