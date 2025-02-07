package com.zb.meeteat.domain.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MatchingResponse {
  private long userid;
  private String message;
  private Place place;
}
