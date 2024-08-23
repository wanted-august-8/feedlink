package com.feedlink.api.feedlink_api.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    @Column(unique = true)
    private String memberEmail;
    private String memberPwd;
    @Column(unique = true)
    private String memberAccount;
    private String memberCode;
    private Boolean memberStatus;

    @Builder
    public Member(Long memberId, String memberEmail, String memberPwd, String memberAccount,
        String memberCode, Boolean memberStatus) {
        this.memberId = memberId;
        this.memberEmail = memberEmail;
        this.memberPwd = memberPwd;
        this.memberAccount = memberAccount;
        this.memberCode = memberCode;
        this.memberStatus = memberStatus;
    }
}
