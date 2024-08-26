package com.feedlink.api.feedlink_api.global.security.autheniation.token;

import static com.feedlink.api.feedlink_api.global.error.ErrorCode.EXPIRED_TOKEN;
import static com.feedlink.api.feedlink_api.global.error.ErrorCode.INVALID_TOKEN;
import static com.feedlink.api.feedlink_api.global.error.ErrorCode.SAVE_REFRESH_TOKEN_FAILED;

import com.feedlink.api.feedlink_api.global.exception.CustomException;
import com.feedlink.api.feedlink_api.global.security.exception.InvalidJwtException;
import com.feedlink.api.feedlink_api.global.security.exception.SaveTokenException;
import com.feedlink.api.feedlink_api.global.redis.RedisService;
import com.feedlink.api.feedlink_api.global.security.autheniation.dto.AuthenticationToken;
import com.feedlink.api.feedlink_api.global.security.autheniation.token.dto.RefreshToken;
import com.feedlink.api.feedlink_api.global.security.autheniation.token.dto.Token;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenService {
    public static final String REDIS_REFERS_TOKEN_PREFIX = "RefreshToken:";
    public static final String REDIS_BLACK_LIST_PREFIX = "BlackList:";
    public static final String BLACK_LIST_DEFAULT_VALUE = "Logout";
    public static final int VALID_MINIMUM_TIME = 0;

    private final TokenProvider tokenProvider;
    private final RedisService redisService;

    /**
     * refreshToken을 이용하여 새로운 accessToken을 발급 받습니다.
     * refreshToken에서 claim을 추출해 redis에 저장된 token과 같은지 비교합니다.
     * 비교한 값이 같다면 새로운 accessToken를 반환합니다.
     *
     * @param refreshToken refreshToken
     * @param accessToken accessToken
     *
     * @return authenticationToken.getAccessToken()
     * */
    public String reissueToken(String refreshToken, String accessToken) {
        try {
            String randomToken = tokenProvider.getSubject(refreshToken);
            log.info("Fetch randomToken = [{}]", randomToken);

            RefreshToken token = redisService.get(REDIS_REFERS_TOKEN_PREFIX + randomToken, RefreshToken.class)
                .orElseThrow(() -> new CustomException(INVALID_TOKEN));
            log.info("Fetch memberId = [{}]", token.getEmail());

            if (!refreshToken.equals(token.getRefreshToken())) {
                throw new CustomException(INVALID_TOKEN);
            }

            addBlackList(accessToken);
            AuthenticationToken authenticationToken = generateNewAccessToken(token.getEmail(), refreshToken);
            return authenticationToken.getAccessToken();
        } catch (ExpiredJwtException e) {
            log.info("[{}] 이미 만료된 토큰입니다.", refreshToken);
            throw new CustomException(EXPIRED_TOKEN);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new CustomException(INVALID_TOKEN);
        }
    }

    /**
     * 새로운 인증토큰을 생성합니다.
     * 인증토큰 생성 후 refreshToken를 새로 생성해 redis에 저장합니다.
     *
     * @param randomToken randomToken
     * @param memberId 회원 ID
     *
     * @return AuthenticationToken
     * */
    public AuthenticationToken generatedToken(String randomToken, String memberId) {
        Token token = tokenProvider.generateToken(randomToken, memberId);
        try {
            RefreshToken refreshToken = createRefreshToken(memberId, token);
            redisService.set(REDIS_REFERS_TOKEN_PREFIX + randomToken, refreshToken, token.getRefreshTokenExpired());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new SaveTokenException(SAVE_REFRESH_TOKEN_FAILED);
        }

        return AuthenticationToken.of(token);
    }

    /**
     * token에 저장된 회원 정보를 조회합니다.
     *
     * @param token 토큰
     *
     * @return 토큰에 저장된 회원정보
     * */
    public String getUsername(String token)
        throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return tokenProvider.getUsername(token);
    }

    /**
     * redis에 저장된 refreshToken를 삭제합니다.
     *
     * @param refreshToken refreshToken
     * */
    public void removeRefreshToken(String refreshToken) {
        try {
            String randomToken = tokenProvider.getSubject(refreshToken);
            log.info("Remove randomToken = [{}]", randomToken);

            if (redisService.delete(REDIS_REFERS_TOKEN_PREFIX + randomToken)) {
                log.info("[{}] RefreshToken 삭제 성공", randomToken);
            }
        } catch (ExpiredJwtException e) {
            log.info("[{}] 이미 만료된 토큰입니다.", refreshToken);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new InvalidJwtException(INVALID_TOKEN, e);
        }
    }

    /**
     * accessToken를 redis의 블랙리스트로 저장합나디.
     *
     * @param accessToken  저장할 accessToken
     * */
    public void addBlackList(String accessToken) {
        try {
            long expiredIn = tokenProvider.getExpiration(accessToken);
            long validTime = expiredIn - Instant.now().getEpochSecond();

            log.info("[{}] AccessToken의 남은 만료 시간은 {}초 입니다.", tokenProvider.getUsername(accessToken), validTime);
            if (validTime > VALID_MINIMUM_TIME) {
                redisService.set(REDIS_BLACK_LIST_PREFIX + accessToken, BLACK_LIST_DEFAULT_VALUE, validTime);
            }
        } catch (ExpiredJwtException e) {
            log.info("[{}] 이미 만료된 토큰입니다.", accessToken);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new InvalidJwtException(INVALID_TOKEN, e);
        }
    }

    public boolean isBlackListToken(String token) {
        return redisService.get(REDIS_BLACK_LIST_PREFIX + token, String.class).isPresent();
    }

    private RefreshToken createRefreshToken(String email, Token token) {
        return RefreshToken.builder()
            .email(email)
            .refreshToken(token.getRefreshToken())
            .build();
    }
    /**
     * 회원 정보와 existingRefreshToken로 새로운 토큰을 생성합니다.
     *
     * @param username      회원정보
     * @param existingRefreshToken 기존에 존재하던 existingRefreshToken
     *
     * @return 새로 생성된 인증토큰
     * */
    private AuthenticationToken generateNewAccessToken(String username, String existingRefreshToken) {
        Token token = tokenProvider.generateTokenWithExistingRefreshToken(username, existingRefreshToken);
        return AuthenticationToken.of(token);
    }
}
