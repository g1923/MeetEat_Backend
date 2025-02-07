package com.zb.meeteat.domain.matching.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zb.meeteat.domain.matching.dto.MatchingRequest;
import com.zb.meeteat.domain.matching.dto.MatchingResponse;
import com.zb.meeteat.domain.matching.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matching")
public class MatchingController {

  private final MatchingService matchingService;

  // 매칭 신청
  @PostMapping("/request")
  public ResponseEntity<MatchingResponse> requestMatching(@RequestBody MatchingRequest req)
      throws JsonProcessingException {

    matchingService.requestMatching(req);

    return ResponseEntity.ok(MatchingResponse.builder()
            .userid(req.getUserId())
            .message("Matching started").build()
    );
  }
}