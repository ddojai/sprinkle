package com.ddojai.github.spinkle.domain.message;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MessageTest {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @DisplayName("뿌리기 토큰 생성 테스트")
  @Test
  public void testGenerateToken() {
    String token = Message.generateToken(new ArrayList<>());

    assertThat(token.length()).isEqualTo(3);
    logger.info(token);
  }
}
