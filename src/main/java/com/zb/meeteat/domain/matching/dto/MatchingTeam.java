package com.zb.meeteat.domain.matching.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("matching-team")
public class MatchingTeam {
  @Id
  private Long teamId;
  private List<MatchingRequest> members = new ArrayList<>();
  private int groupSize;    // 팀의 크기
  private double placeLon;  // 팀 대표 식당의 경도
  private double placeLat;  // 팀 대표 식당의 위도

  @TimeToLive
  private long timeToLive = 600; // 10분 후 자동 삭제
  private LocalDateTime matchingFinishTime;

  public boolean isFull() {
    return members.size() >= groupSize;
  }

  public void addMember(MatchingRequest member) {
    members.add(member);
  }
}