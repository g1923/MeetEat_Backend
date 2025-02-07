package com.zb.meeteat.domain.matching.service;

import com.zb.meeteat.domain.matching.dto.MatchingRequest;
import com.zb.meeteat.domain.matching.redis.RedisClient;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingService {

  private final RedisClient redisClient;
  private final RedisTemplate<String, Object> redisTemplate;
  private final String MATCHING_QUEUE = "matching_queue:";

  // 매칭 신청
  public void requestMatching(MatchingRequest req) {

    String key = String.join(MATCHING_QUEUE, ":" + req.getUserId());

    // 10분간 매칭 유지
    LocalDateTime now = LocalDateTime.now();
    req.setMatchingStartTime(now.toString());
    int ttl = (int) Duration.between(now, now.plusSeconds(10 * 60)).getSeconds();

    redisClient.put(key, req);
    redisClient.expire(key, ttl);
  }
}
