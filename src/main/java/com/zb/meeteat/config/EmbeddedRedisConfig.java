package com.zb.meeteat.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

@Slf4j
@Configuration
public class EmbeddedRedisConfig {

  private RedisServer redisServer;

  @Value("${spring.data.redis.port}")
  private int port;

  @PostConstruct
  public void startRedis() throws IOException {
    redisServer = RedisServer.builder().port(port).build();

    try {
      log.info("✅ Embedded Redis Server started on port 6379");
      redisServer.start();
    } catch (RuntimeException e) {
      log.info("🛑 Embedded Redis Server RuntimeException");
    }
  }

  @PreDestroy
  public void stopRedis() {
    redisServer.stop();
    log.info("🛑 Embedded Redis Server stopped");
  }
}
