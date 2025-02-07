package com.zb.meeteat.domain.matching.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zb.meeteat.domain.matching.dto.MatchingRequest;
import com.zb.meeteat.domain.matching.dto.MatchingTeam;
import com.zb.meeteat.domain.matching.repository.MatchingTeamRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingService {

//  private final RedisClient redisClient;
//  private final RedisTemplate<String, Object> redisTemplate;
//  private static final ObjectMapper mapper = new ObjectMapper();
//  private final String MATCHING_QUEUE = "matching_queue:";

  private final MatchingQueue matchingQueue;
  private final MatchingTeamRepository matchingTeamRepository;

  // 매칭 신청
//  public void requestMatching(MatchingRequest req) {
//
//    String key = String.join(MATCHING_QUEUE, ":" + req.getUserId());
//
//    // 10분간 매칭 유지
//    LocalDateTime now = LocalDateTime.now();
//    req.setMatchingStartTime(now.toString());
//    int ttl = (int) Duration.between(now, now.plusSeconds(10 * 60)).getSeconds();
//
//    redisClient.put(key, req);
//    redisClient.expire(key, ttl);
//  }

  // 매칭 신청
  public void requestMatching(MatchingRequest request) throws JsonProcessingException {
    // 10분간 매칭 유지
    request.setMatchingStartTime(LocalDateTime.now().toString());
    request.setTimeToLive(10 * 60);

    matchingQueue.addMappingQueue(request);
  }

  public void makeMatchingTeam() throws JsonProcessingException {

    // 대기큐가 존재할때만 매칭 진행
    while (matchingQueue.isMatchingQueueExist()) {

      long teamCount = matchingTeamRepository.count();
      if (teamCount < 1) {
        log.info("➡️ 현재 생성된 대기팀 비어있음..");

        MatchingRequest leader = matchingQueue.getFirstUserFromQueue();
        matchingQueue.createTeam(leader);
      } else {
        log.info("➡️ 대기팀이 존재! 매칭 시작!");

        boolean matched = false;
        MatchingRequest newMember = matchingQueue.getFirstUserFromQueue();
        List<MatchingTeam> waitingTeam = (List<MatchingTeam>) matchingTeamRepository.findAll();
        for (MatchingTeam team : waitingTeam) {
          // 팀 매칭인원 부족
          if (team.getMembers().size() < team.getGroupSize()
              && team.getGroupSize() == newMember.getGroupSize()
              && within2km(
              newMember.getUserLat(), newMember.getUserLon(),
              team.getPlaceLat(), team.getPlaceLon()
          )) {
            team.addMember(newMember);
            log.info("➡️ 유저 {} 를 waitingTeam {} 에 추가", newMember.getUserId(), team.getTeamId());
            matched = true;

            if (team.isFull()) {
              // todo team 전체에게 완료 알림가기
              // db에 데이터 저장하기
            }

            break;
          }

          if (!matched) {
            log.info("➡️ 현존하는 팀에는 매칭불가! 유저 {}를 위한 새로운 대기팀 생성", newMember.getUserId());
            matchingQueue.createTeam(newMember);
          }
        }
      }
    }
  }

  // 대기자 위치와 팀내 식당위치가 2km이내에 있는지 확인
  private boolean within2km(double userLat, double userLon, double placeLat, double placeLon) {

    double distance = calculateHaversine(userLat, userLon, placeLat, placeLon);

    log.info("⚠️ 2km이내에 존재하나? {},  {}",distance,  (distance <= 2.0));
    return distance <= 2.0; // 2km 반경안에 있다.
  }

  // 지도에서 좌표 간의 거리를 구할 때는 하버사인(Haversine) 공식을 사용해 지구의 곡률까지 계산
  private double calculateHaversine(double userLat, double userLon, double placeLat, double placeLon) {
    log.info("⚠️ userLat={}, userLon={}, placeLat={}, placeLon={}", userLat, userLon, placeLat, placeLon);

    // 위도, 경도를 라디안으로 변환
    double dLat = Math.toRadians(placeLat - userLat);
    double dLon = Math.toRadians(placeLon - userLon);

    double lat1Rad = Math.toRadians(userLat);
    double lat2Rad = Math.toRadians(placeLat);

    // Haversine 공식 적용
    double a = (Math.pow(Math.sin(dLat / 2), 2))
        + ((Math.cos(lat1Rad)) * (Math.cos(lat2Rad)) * (Math.pow(Math.sin(dLon / 2), 2)));

    // 두 점 사이의 대원 거리(구의 표면을 따라 연결된 최단 거리)를 계산
    // Math.atan2()를 사용하여 두 점 사이의 실제 거리 값을 계산.
    double c = (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));

    double EARTH_RADIUS = 6371.0; // 지구 반지름 (단위: km)
    return EARTH_RADIUS * c; // 결과 거리 (단위: km)
  }


}
