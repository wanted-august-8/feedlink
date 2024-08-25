package com.feedlink.api.feedlink_api.global.security.autheniation.token;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static org.springframework.security.config.Elements.JWT;

import com.feedlink.api.feedlink_api.global.security.autheniation.token.dto.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenProvider {
    private static final int MILLISECONDS_TO_SECONDS = 1000;
    private static final int TOKEN_REFRESH_INTERVAL = 24;
    private static final String TOKEN_CLAIM_KEY = "username";
    private static final String TOKEN_EXPIRATION_KEY = "exp";

    private final Key key;
    private final String grantType;
    private final Long accessTokenExpiredTime;
    private final Long refreshTokenExpiredTime;

    public TokenProvider(
        @Value("${jwt.secret}") String secretKey,
        @Value("${jwt.grant-type}") String grantType,
        @Value("${jwt.token-validate-in-seconds}") Long tokenValidateInSeconds
    ) {
        this.key = getSecretKey(secretKey);
        this.grantType = grantType;
        this.accessTokenExpiredTime = tokenValidateInSeconds;
        this.refreshTokenExpiredTime = tokenValidateInSeconds * TOKEN_REFRESH_INTERVAL;
    }

    /** Token 생성 */
    public Token generateToken(String randomToken, String username) {
        return Token.builder()
            .grantType(grantType)
            .refreshToken(createRefreshToken(randomToken))
            .refreshTokenExpired(refreshTokenExpiredTime)
            .accessToken(createAccessToken(username))
            .accessTokenExpired(accessTokenExpiredTime)
            .build();
    }

    public String getUsername(String token)
        throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        Claims claims = getClaims(token);
        String claim = claims.get(TOKEN_CLAIM_KEY, String.class);
        if (Objects.isNull(claim)) {
            throw new IllegalArgumentException();
        }
        return claim;
    }

    public Long getExpiration(String token)
        throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        Claims claims = getClaims(token);
        return claims.get(TOKEN_EXPIRATION_KEY, Long.class);
    }

    public String getSubject(String token)
        throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    private String createAccessToken(String username) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessTokenExpiredTime * MILLISECONDS_TO_SECONDS);

        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(expireDate)
            .claim(TOKEN_CLAIM_KEY, username)
            .signWith(key, HS256)
            .compact();
    }

    private String createRefreshToken(String randomToken) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshTokenExpiredTime * MILLISECONDS_TO_SECONDS);

        return Jwts.builder()
            .setHeader(createHeader())
            .setSubject(randomToken)
            .setIssuedAt(now)
            .setExpiration(expireDate)
            .signWith(key, HS256)
            .compact();
    }

    public Token generateTokenWithExistingRefreshToken(String username, String existingRefreshToken) {
        return Token.builder()
            .grantType(grantType)
            .refreshToken(existingRefreshToken)
            .refreshTokenExpired(getExpiration(existingRefreshToken))
            .accessToken(createAccessToken(username))
            .accessTokenExpired(accessTokenExpiredTime)
            .build();
    }

    private SecretKey getSecretKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /** JWT의 헤더(Header)를 생성 */
    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("alg", HS256.getValue());
        header.put("typ", JWT);
        return header;
    }

    /** 주어진 JWT 토큰에서 클레임(Claims)을 추출 */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
