package com.ddojai.github.spinkle.controller;

import com.ddojai.github.spinkle.domain.message.Message;
import com.ddojai.github.spinkle.domain.room.Room;
import com.ddojai.github.spinkle.domain.user.User;
import com.ddojai.github.spinkle.dto.*;
import com.ddojai.github.spinkle.service.MessageService;
import com.ddojai.github.spinkle.service.RoomService;
import com.ddojai.github.spinkle.service.RoomUserService;
import com.ddojai.github.spinkle.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

  @Autowired
  private UserService userService;

  @Autowired
  private RoomService roomService;

  @Autowired
  private RoomUserService roomUserService;

  @Autowired
  private MessageService messageService;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @DisplayName("뿌리기 API 테스트")
  @Test
  public void testSend() throws Exception {
    // data
    User sendUser = userService.save("test1-1", 1000000);
    Room room = roomService.save();
    roomUserService.save(room, sendUser);

    // given
    String url = "http://localhost:" + port + "/api/messages/money";
    int sendAmount = 10000;
    int numberPeople = 5;
    MoneySendRequestDto requestDto = MoneySendRequestDto.builder()
      .sendAmount(sendAmount)
      .numberPeople(numberPeople)
      .build();
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-ROOM-ID", room.getCode());
    headers.set("X-USER-ID", String.valueOf(sendUser.getId()));
    HttpEntity<MoneySendRequestDto> requestEntity = new HttpEntity<>(requestDto, headers);

    // when
    ResponseEntity<MoneySendResponseDto> responseEntity = restTemplate.postForEntity(url,
      requestEntity,
      MoneySendResponseDto.class);
    MoneySendResponseDto responseDto = responseEntity.getBody();

    // then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(Objects.requireNonNull(responseDto).getToken().length()).isEqualTo(3);
    logger.info(Objects.requireNonNull(responseDto).getToken());
  }

  @DisplayName("받기 API 테스트")
  @Test
  public void testReceive() throws Exception {
    // data
    User sendUser = userService.save("test2-1", 1000000);
    User receiveUser = userService.save("test2-2", 1000000);
    Room room = roomService.save();
    roomUserService.save(room, sendUser);
    roomUserService.save(room, receiveUser);

    MoneySendRequestDto requestDto = MoneySendRequestDto.builder()
      .sendAmount(1000)
      .numberPeople(1)
      .build();
    String token = Message.generateToken(new ArrayList<>());
    messageService.sendMoney(sendUser.getId(), room, token, requestDto);

    // given
    String url = "http://localhost:" + port + "/api/messages/" + token + "/money";

    HttpHeaders headers = new HttpHeaders();
    headers.set("X-ROOM-ID", room.getCode());
    headers.set("X-USER-ID", String.valueOf(receiveUser.getId()));
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
  public void testCheck() throws Exception {
    // data
    User sendUser = userService.save("test3-1", 1000000);
    Room room = roomService.save();
    roomUserService.save(room, sendUser);

    MoneySendRequestDto requestDto = MoneySendRequestDto.builder()
      .sendAmount(1000)
      .numberPeople(1)
      .build();
    String token = Message.generateToken(new ArrayList<>());
    messageService.sendMoney(sendUser.getId(), room, token, requestDto);

    // given
    String url = "http://localhost:" + port + "/api/messages/" + token + "/money";

    HttpHeaders headers = new HttpHeaders();
    headers.set("X-ROOM-ID", room.getCode());
    headers.set("X-USER-ID", String.valueOf(sendUser.getId()));
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
