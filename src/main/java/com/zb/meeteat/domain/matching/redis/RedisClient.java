package com.zb.meeteat.domain.matching.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisClient {

  private final RedisTemplate<String, Object> redisTemplate;
  private static final ObjectMapper mapper = new ObjectMapper();

  public <T> T get(Long key, Class<T> classType) {
    return get(key.toString(), classType);
  }

  private <T> T get(String key, Class<T> classType) {
    try {
      // Redis 리스트에서 가장 먼저 들어온 데이터를 추출 (왼쪽 끝에서 추출)
      String value = (String) redisTemplate.opsForList().leftPop(key);
      if (ObjectUtils.isEmpty(value)) {
        return null;
      }

      // 직렬화된 값을 해당 타입으로 변환하여 반환
      return mapper.readValue(value, classType);
    } catch (JsonProcessingException e) {
      log.error("❌ Parsing error", e);
    }

    return null;
  }

  public <T> void put(String key, T object) {
    try {
      // 객체를 Redis 리스트에 직렬화하여 추가
      redisTemplate.opsForList().rightPush(key, mapper.writeValueAsString(object));

    } catch (JsonProcessingException e) {
      log.error("❌ Parsing error", e);
    }
  }

  public <T> void expire (String key, Integer ttlInSeconds) {
    if (ttlInSeconds != null) {
      redisTemplate.expire(key, ttlInSeconds, TimeUnit.SECONDS);
    }
  }

  public <T> void delete(String key) {
    try {
      redisTemplate.delete(key);  // Redis에서 해당 key를 삭제
    } catch (Exception e) {
      log.error("❌ Redis delete error", e);
    }
  }

}
