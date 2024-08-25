package com.feedlink.api.feedlink_api.domain.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.feedlink.api.feedlink_api.domain.member.entity.Member;
import com.feedlink.api.feedlink_api.domain.security.constants.JwtConstants;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtils {

    // Access Token 에는 id를 담는다
    public static String generateAccessToken(Member member) {

        return JWT.create()
            .withSubject(JwtConstants.ACCESS)
            .withHeader(createHeader())
            .withClaim("id", member.getMemberId())
            .withExpiresAt(createExpireDate(JwtConstants.ACCESS_EXP_TIME))
            .sign(Algorithm.HMAC512(JwtConstants.SECRET_KEY));
    }

    // Refresh Token 에는 아무것도 담지 않는다
    public static String generateRefreshToken(Member member) {
        return JWT.create()
            .withSubject(JwtConstants.REFRESH)
            .withHeader(createHeader())
            .withExpiresAt(createExpireDate(JwtConstants.REFRESH_EXP_TIME))
            .sign(Algorithm.HMAC512(JwtConstants.SECRET_KEY));
    }

    // 헤더 정보 생성
    private static Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS512");
        return header;
    }

    // 만료 시간 생성
    private static Date createExpireDate(long expireTime) {
        return new Date(System.currentTimeMillis() + expireTime);
    }


    // 헤더에 Bearer XXX 형식으로 담겨온 토큰을 추출한다
    public static String getTokenFromHeader(String header) {
        return header.split(" ")[1];
    }


    // 토큰 유효성 검사 => 여러 예외가 발생함 ( 호출하는 곳에서 처리 필요 )
    public static DecodedJWT verifyToken(String token) {
        return JWT.require(Algorithm.HMAC512(JwtConstants.SECRET_KEY)).build().verify(token);
    }

    public static UsernamePasswordAuthenticationToken getAuthenticationToken(DecodedJWT decodedJWT) {
        String id = decodedJWT.getClaim("id").asString();

        return new UsernamePasswordAuthenticationToken(id, null);
    }
}