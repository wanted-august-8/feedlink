package com.feedlink.api.feedlink_api.domain.security.service;

import static com.feedlink.api.feedlink_api.global.error.ErrorCode.LOGIN_FAIL;

import com.feedlink.api.feedlink_api.domain.member.entity.Member;
import com.feedlink.api.feedlink_api.domain.member.repository.MemberRepository;
import com.feedlink.api.feedlink_api.domain.security.PrincipalDetails;
import com.feedlink.api.feedlink_api.domain.security.dto.ReissueRequest;
import com.feedlink.api.feedlink_api.global.security.autheniation.token.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final TokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException(LOGIN_FAIL.getMessage()));

        log.info("LoadMember member = {}", member);
        return new PrincipalDetails(member);
    }

    public String reissue(ReissueRequest reissueRequest) {
        return tokenService.reissueToken(reissueRequest.getRefreshToken(),
            reissueRequest.getAccessToken());
    }
}
