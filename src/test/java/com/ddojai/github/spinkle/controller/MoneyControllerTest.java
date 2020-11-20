package com.ddojai.github.spinkle.controller;

import com.ddojai.github.spinkle.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MoneyControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  private HttpHeaders headers;

  @BeforeEach
  private void setUp() {
    headers = new HttpHeaders();
    headers.set("X-USER-ID", "1");
    headers.set("X-ROOM-ID", "dljflsdjfljeiajfljf");
  }

  @DisplayName("뿌리기 API 테스트")
  @Test
  public void testSend() {
    // given
    String url = "http://localhost:" + port + "/api/messages/money";

    int sendAmount = 10000;
    int numberPeople = 5;
    MoneySendRequestDto requestDto = MoneySendRequestDto.builder()
      .sendAmount(sendAmount)
      .numberPeople(numberPeople)
      .build();
    HttpEntity<MoneySendRequestDto> requestEntity = new HttpEntity<>(requestDto, headers);

    // when
    ResponseEntity<MoneySendResponseDto> responseEntity = restTemplate.postForEntity(url,
      requestEntity,
      MoneySendResponseDto.class);
    MoneySendResponseDto responseDto = responseEntity.getBody();

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(Objects.requireNonNull(responseDto).getToken().length()).isEqualTo(3);
  }

  @DisplayName("받기 API 테스트")
  @Test
  public void testReceive() {
    // given
    String token = "asd";
    String url = "http://localhost:" + port + "/api/messages/" + token + "/money";

    HttpEntity<String> requestEntity = new HttpEntity<>("", headers);

    // when
    ResponseEntity<MoneyReceiveResponseDto> responseEntity = restTemplate.exchange(url,
      HttpMethod.PUT, requestEntity, MoneyReceiveResponseDto.class);
    MoneyReceiveResponseDto responseDto = responseEntity.getBody();

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(Objects.requireNonNull(responseDto).getReceivedAmount()).isGreaterThan(0);
  }

  @DisplayName("조회 API 테스트")
  @Test
  public void testCheck() {
    // given
    String token = "asd";
    String url = "http://localhost:" + port + "/api/messages/" + token + "/money";

    HttpEntity<String> requestEntity = new HttpEntity<>("", headers);

    // when
    ResponseEntity<MoneyCheckResponseDto> responseEntity = restTemplate.exchange(url,
      HttpMethod.GET, requestEntity, MoneyCheckResponseDto.class);
    MoneyCheckResponseDto responseDto = responseEntity.getBody();
    List<ReceivedUserDto> receivedUserDtos =
      Objects.requireNonNull(responseDto).getReceivedUserDtos();

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseDto.getSendDate()).isBefore(LocalDateTime.now());
    assertThat(Objects.requireNonNull(responseDto).getTotalAmount()).isGreaterThan(0);
    assertThat(Objects.requireNonNull(responseDto).getReceivedCompletedAmount()).isGreaterThanOrEqualTo(0);
    for (ReceivedUserDto receivedUserDto : receivedUserDtos) {
      assertThat(receivedUserDto.getUserId()).isGreaterThan(0);
      assertThat(receivedUserDto.getReceivedAmount()).isGreaterThan(0);
    }
  }
}
