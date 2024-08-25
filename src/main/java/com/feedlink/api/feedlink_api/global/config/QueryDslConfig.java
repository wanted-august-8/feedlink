package com.feedlink.api.feedlink_api.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@EnableJpaRepositories(basePackages = {
    "com.feedlink.api.feedlink_api.domain.member.repository",
    "com.feedlink.api.feedlink_api.domain.post.repository",
    "com.feedlink.api.feedlink_api.domain.hashtag.repository",
    "com.feedlink.api.feedlink_api.domain.posthashtag.repository",
    "com.feedlink.api.feedlink_api.domain.statistic.repository"
})
public class QueryDslConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
