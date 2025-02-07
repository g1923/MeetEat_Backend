package com.zb.meeteat.domain.matching.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zb.meeteat.domain.matching.dto.MatchingRequest;
import com.zb.meeteat.domain.matching.dto.MatchingTeam;
import com.zb.meeteat.domain.matching.repository.MatchingTeamRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingQueue {
  private final String MATCHING_QUEUE = "matching_queue";

  private final RedisTemplate<String, Object> redisTemplate;
  private static final ObjectMapper mapper = new ObjectMapper();

  private final MatchingTeamRepository matchingTeamRepository;

  // 매칭큐에 유저 추가
  public void addMappingQueue(MatchingRequest member) throws JsonProcessingException {
    log.info("➡️ 대기큐에 유저 추가");
    redisTemplate.opsForList().leftPush(MATCHING_QUEUE, mapper.writeValueAsString(member));
  }

  // 대기큐에서 첫번째 유저 가져오기
  public MatchingRequest getFirstUserFromQueue() throws JsonProcessingException {
    String userJson = (String) redisTemplate.opsForList().leftPop(MATCHING_QUEUE);
    log.info("📌 대기큐에서 데이터 뽑기 \n {}", userJson);
    if (userJson != null) {
      return mapper.readValue(userJson, MatchingRequest.class);
    }
    return null;
  }

  // 현재 대기큐 확인
  public boolean isMatchingQueueExist() {

    if (redisTemplate == null || !redisTemplate.hasKey(MATCHING_QUEUE)) {
      return false;
    }
    return redisTemplate.opsForList().size(MATCHING_QUEUE) > 0;
  }

  public void createTeam(MatchingRequest user) {
    MatchingTeam newTeam = MatchingTeam.builder()
        .members(new ArrayList<MatchingRequest>(List.of(user)))
        .groupSize(user.getGroupSize())
        .placeLon(user.getPlace().getLon())
        .placeLat(user.getPlace().getLat())
        .timeToLive(user.getTimeToLive())
        .build();

    matchingTeamRepository.save(newTeam);
    log.info("➡️ 새로운 팀 생성 : leader = {}", user.getUserId());
  }


}
