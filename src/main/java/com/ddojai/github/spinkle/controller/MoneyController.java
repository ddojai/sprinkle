package com.ddojai.github.spinkle.controller;

import com.ddojai.github.spinkle.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MoneyController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  // 뿌리기
  @PostMapping("/api/messages/money")
  public ResponseEntity<MoneySendResponseDto> send(@RequestHeader(value = "X-USER-ID") String userId,
                                                   @RequestHeader(value = "X-ROOM-ID") String code,
                                                   @RequestBody MoneySendRequestDto requestDto) {
    logger.info(userId);
    logger.info(code);
    logger.info(String.valueOf(requestDto.getNumberPeople()));
    logger.info(String.valueOf(requestDto.getSendAmount()));

    String token = "asd";

    return new ResponseEntity<>(new MoneySendResponseDto(token), HttpStatus.CREATED);
  }

  // 받기
  @PutMapping("/api/messages/{token}/money")
  public ResponseEntity<MoneyReceiveResponseDto> receive(@RequestHeader(value = "X-USER-ID") String userId,
                                                         @RequestHeader(value = "X-ROOM-ID") String code,
                                                         @PathVariable String token) {
    logger.info(userId);
    logger.info(code);
    logger.info(token);

    int receivedAmount = 100;

    return ResponseEntity.ok(new MoneyReceiveResponseDto(receivedAmount));
  }

  // 조회
  @GetMapping("/api/messages/{token}/money")
  public ResponseEntity<MoneyCheckResponseDto> check(@RequestHeader(value = "X-USER-ID") String userId,
                                                     @RequestHeader(value = "X-ROOM-ID") String code,
                                                     @PathVariable String token) {
    logger.info(userId);
    logger.info(code);
    logger.info(token);

    LocalDateTime sendDate = LocalDateTime.of(2020, 11, 1, 0, 0, 0);
    List<ReceivedUserDto> receivedUserDtos = new ArrayList<>();

    MoneyCheckResponseDto moneyCheckResponseDto = MoneyCheckResponseDto.builder()
      .sendDate(sendDate)
      .totalAmount(10000)
      .receivedCompletedAmount(0)
      .receivedUserDtos(receivedUserDtos)
      .build();
    return ResponseEntity.ok(moneyCheckResponseDto);
  }
}
