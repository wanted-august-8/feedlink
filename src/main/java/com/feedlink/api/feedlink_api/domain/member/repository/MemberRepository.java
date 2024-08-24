package com.feedlink.api.feedlink_api.domain.member.repository;

import com.feedlink.api.feedlink_api.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberEmail(String memberEmail);
    Optional<Member> findByMemberAccount(String memberAccount);
}
