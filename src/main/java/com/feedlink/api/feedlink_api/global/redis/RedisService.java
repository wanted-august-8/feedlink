package com.feedlink.api.feedlink_api.global.redis;

import static com.feedlink.api.feedlink_api.global.error.ErrorCode.COMMON_SYSTEM_ERROR;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.feedlink.api.feedlink_api.global.exception.CustomException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * key로 데이터를 조회하고, type으로 지정된 객체 타입으로 변환해서 반환합니다.
     *
     * @param key       저장된 key값
     * @param type      변환하려는 객체 타입
     * @return  저장된 key의 값을 원하는 객체 타입으로 변환하여 반환
     * */
    public <T> Optional<T> get(String key, Class<T> type) {
        log.info("Find Redis Key = [{}], Type = [{}]", key, type.getName());
        String serializedValue = redisTemplate.opsForValue().get(key);

        try {
            return Optional.of(objectMapper.readValue(serializedValue, type));
        } catch (IllegalArgumentException | InvalidFormatException e) {
            return Optional.empty();
        } catch (Exception e) {
            log.error("Redis Get Exception", e);
            throw new CustomException("Redis get() Error", COMMON_SYSTEM_ERROR);
        }
    }

    /**
     * redis에 데이터를 저장합니다.
     *
     * @param key           저장될 key값
     * @param value         저장하려는 데이터
     * @param expiredTime   데이터 만료시간
     * */
    public void set(String key, Object value, Long expiredTime) {
        log.info("Save Redis Key = [{}], Value = [{}], expiredTime = [{}]", key, value, expiredTime);
        try {
            String serializedValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, serializedValue, expiredTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Redis Set Exception", e);
            throw new CustomException("Redis get() Error", COMMON_SYSTEM_ERROR);
        }
    }

    /**
     * Redis에 데이터 저장 성공 여부를 확인합니다.
     *
     * @param key           저장될 key값
     * @param value         저장하려는 데이터
     * @param expiredTime   데이터 만료시간
     * @return              저장 성공 여부를 나타내는 boolean 값
     * */
    public boolean setIfAbsent(String key, Object value, Long expiredTime) {
        log.info("Save If Absent Redis Key = [{}], Value = [{}], expiredTime = [{}]", key, value, expiredTime);
        try {
            String serializedValue = objectMapper.writeValueAsString(value);
            return Boolean.TRUE.equals(
                redisTemplate.opsForValue().setIfAbsent(
                    key,
                    serializedValue,
                    expiredTime, TimeUnit.SECONDS
                ));
        } catch (Exception e) {
            log.error("Redis Set Exception", e);
            throw new CustomException("Redis get() Error", COMMON_SYSTEM_ERROR);
        }
    }

    /**
     * 저장된 데이터를 삭제합니다..
     *
     * @param key   삭제할 데이터의 key값
     * @return      삭제 성공 여부를 나타내는 boolean 값
     */
    public boolean delete(String key) {
        log.info("Delete Redis Key = [{}]", key);
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }
}
