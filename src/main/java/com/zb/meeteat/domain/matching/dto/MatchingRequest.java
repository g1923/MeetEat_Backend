package com.zb.meeteat.domain.matching.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchingRequest {

  private Long userId;               // TODO: 토큰받으면 토큰에서 userId값 받아오는것으로 수정
  private double userLon;           // 유저 경도
  private double userLat;           // 유저 위도
  private int groupSize;            // 희망 인원수
  private Place place;

  private String matchingStartTime; // 매칭시작시간
}