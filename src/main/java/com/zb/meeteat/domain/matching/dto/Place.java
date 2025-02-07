package com.zb.meeteat.domain.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Place {
  private Long id;                // kakao maps api 키
  private String name;            // 장소 이름
  private String category_name;    // 장소 카테고리
  private String road_address_name; // 도로명 주소
  private String phone;
  private double lon;             // 경도
  private double lat;             // 위도
  private String place_url;       // 상세정보url
}
