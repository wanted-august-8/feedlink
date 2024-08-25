package com.feedlink.api.feedlink_api.domain.member.service;

import com.feedlink.api.feedlink_api.domain.member.dto.MemberSignupRequest;
import com.feedlink.api.feedlink_api.domain.member.entity.Member;
import com.feedlink.api.feedlink_api.domain.member.repository.MemberRepository;
import com.feedlink.api.feedlink_api.domain.member.util.PasswordValidator;
import com.feedlink.api.feedlink_api.global.common.CommonResponse;
import com.feedlink.api.feedlink_api.global.error.ErrorCode;
import com.feedlink.api.feedlink_api.global.exception.CustomException;
import java.util.Optional;
import java.util.Random;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    /**
     * 회원가입 요청을 처리하는 메서드입니다.
     * 요청을 검증하고, 회원을 생성한 후 데이터베이스에 저장합니다.
     *
     * @param request 회원가입 요청을 담은 DTO 객체
     * @return CommonResponse<String> 성공 메시지 반환
     */
    @Transactional
    public CommonResponse<String> signup(MemberSignupRequest request) {
        validateSignupRequest(request);
        String encryptedPassword = passwordEncoder.encode(request.getMemberPwd());
        Member member = Member.builder()
            .memberEmail(request.getMemberEmail())
            .memberPwd(encryptedPassword)
            .memberAccount(request.getMemberAccount())
            .memberStatus(false) // 가입승인 전까지 비활성화 상태
            .memberCode(generateVerificationCode()) // 인증 코드 설정
            .build();
        memberRepository.save(member);
        return CommonResponse.ok("가입 승인 메일을 보냈습니다. 가입 승인을 완료해 주세요.", null);
    }

    /**
     * 회원가입 요청을 검증하는 메서드입니다.
     * 이메일과 계정 중복을 검사하고, 비밀번호 조건을 확인합니다.
     *
     * @param request 회원가입 요청을 담은 DTO 객체
     */
    private void validateSignupRequest(MemberSignupRequest request) {
        Optional<Member> memberOptional = memberRepository.findByMemberEmailOrMemberAccount(request.getMemberEmail(), request.getMemberAccount());

        if (memberOptional.isPresent()) {
            Member existingMember = memberOptional.get();

            // 이메일 중복 검사
            if (existingMember.getMemberEmail().equals(request.getMemberEmail())) {
                throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
            }

            // 계정 중복 검사
            if (existingMember.getMemberAccount().equals(request.getMemberAccount())) {
                throw new CustomException(ErrorCode.ACCOUNT_ALREADY_EXISTS);
            }
        }

        // 비밀번호 조건 검사
        PasswordValidator.validatePassword(request.getMemberPwd(), null, request.getMemberAccount(), request.getMemberEmail());
    }

    /**
     * 6자리 랜덤 인증 코드를 생성하는 메서드입니다.
     *
     * @return 6자리 랜덤 인증 코드 문자열
     */
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6자리 랜덤 숫자 생성
        return String.valueOf(code);
    }
}