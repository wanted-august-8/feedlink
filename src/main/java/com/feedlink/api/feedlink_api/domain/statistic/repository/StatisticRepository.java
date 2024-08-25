package com.feedlink.api.feedlink_api.domain.statistic.repository;


import com.feedlink.api.feedlink_api.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticRepository extends JpaRepository<Post,Long>, StatisticRepositoryCustom {

}
