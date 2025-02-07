package com.zb.meeteat.domain.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("matching-queue")
public class MatchingRequest {

  private Long userId;               // TODO: 토큰받으면 토큰에서 userId값 받아오는것으로 수정
  private double userLon;           // 유저 경도
  private double userLat;           // 유저 위도
  private int groupSize;            // 희망 인원수
  private Place place;

  private String matchingStartTime; // 매칭시작시간

  @TimeToLive
  private long timeToLive;

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Place {
    private Long id;                // kakao maps api 키
    private String name;            // 장소 이름
    private String category_name;    // 장소 카테고리
    private String road_address_name; // 도로명 주소
    private String phone;
    private double lon;             // 경도
    private double lat;             // 위도
    private String place_url;       // 상세정보url
  }
}