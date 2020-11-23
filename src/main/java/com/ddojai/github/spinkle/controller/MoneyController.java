package com.ddojai.github.spinkle.controller;

import com.ddojai.github.spinkle.domain.message.Message;
import com.ddojai.github.spinkle.domain.money.Money;
import com.ddojai.github.spinkle.domain.room.Room;
import com.ddojai.github.spinkle.dto.*;
import com.ddojai.github.spinkle.service.MessageService;
import com.ddojai.github.spinkle.service.RoomService;
import com.ddojai.github.spinkle.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class MoneyController {

  private final UserService userService;
  private final RoomService roomService;
  private final MessageService messageService;

  @PostMapping("/api/messages/money")
  public ResponseEntity<MoneySendResponseDto> send(@RequestHeader(value = "X-USER-ID") String xUserId,
                                                   @RequestHeader(value = "X-ROOM-ID") String xRoomId,
                                                   @RequestBody MoneySendRequestDto requestDto) {
    Long userId = Long.parseLong(xUserId);
    Room room = roomService.findByCode(xRoomId);
    room.isUserExist(userId);

    List<Message> messages = messageService.findAllByRoom(room);
    List<String> existTokens = messages.stream()
      .map(Message::getToken)
      .collect(Collectors.toList());
    String token = Message.generateToken(existTokens);

    messageService.sendMoney(userId, room, token, requestDto);

    return new ResponseEntity<>(new MoneySendResponseDto(token), HttpStatus.CREATED);
  }

  @PutMapping("/api/messages/{token}/money")
  public ResponseEntity<MoneyReceiveResponseDto> receive(@RequestHeader(value = "X-USER-ID") String xUserId,
                                                         @RequestHeader(value = "X-ROOM-ID") String xRoomId,
                                                         @PathVariable String token) {
    Long userId = Long.parseLong(xUserId);
    Room room = roomService.findByCode(xRoomId);
    Message message = messageService.findByRoomAndToken(room, token);
    if (message.getUser().getId().equals(userId)) {
      throw new IllegalArgumentException("뿌린 당사자는 받을 수 없습니다");
    }
    room.isUserExist(userId);
    if (message.getCreatedDate().plusMinutes(10).isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException("받을 수 있는 10분의 유효시간이 지났습니다.");
    }
    List<Money> moneyList = message.getMoneyList();
    Money money = null;
    for (Money element : moneyList) {
      Long receivedUserId = element.getReceivedUserId();
      if (receivedUserId.equals(userId)) {
        throw new IllegalArgumentException("이미 받았습니다");
      }
      if (receivedUserId == 0) {
        money = element;
      }
    }
    if (money == null) {
      throw new IllegalArgumentException("받을 수 있는 돈이 남지 않았습니다.");
    }

    int receivedAmount = userService.receiveMoney(userId, money);

    return ResponseEntity.ok(new MoneyReceiveResponseDto(receivedAmount));
  }

  @GetMapping("/api/messages/{token}/money")
  public ResponseEntity<MoneyCheckResponseDto> check(@RequestHeader(value = "X-USER-ID") String xUserId,
                                                     @RequestHeader(value = "X-ROOM-ID") String xRoomId,
                                                     @PathVariable String token) {
    Long userId = Long.parseLong(xUserId);
    Room room = roomService.findByCode(xRoomId);
    Message message = messageService.findByRoomAndToken(room, token);
    if (!message.getUser().getId().equals(userId)) {
      throw new IllegalArgumentException("뿌린 당사자가 아니면 조회할 수 없습니다.");
    }
    if (message.getCreatedDate().plusDays(7).isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException("7일이 지나면 조회할 수 없습니다.");
    }

    List<ReceivedUserDto> receivedUserDtos = new ArrayList<>();
    int receivedCompletedAmount = 0;
    for (Money money : message.getMoneyList()) {
      if (money.getReceivedUserId() > 0) {
        receivedUserDtos.add(ReceivedUserDto.builder()
          .userId(money.getReceivedUserId())
          .receivedAmount(money.getMoneyAmount())
          .build()
        );
        receivedCompletedAmount += money.getMoneyAmount();
      }
    }

    MoneyCheckResponseDto moneyCheckResponseDto = MoneyCheckResponseDto.builder()
      .sendDate(message.getCreatedDate())
      .totalAmount(message.getMoneyAmount())
      .receivedCompletedAmount(receivedCompletedAmount)
      .receivedUserDtos(receivedUserDtos)
      .build();

    return ResponseEntity.ok(moneyCheckResponseDto);
  }
}
