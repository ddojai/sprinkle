package com.ddojai.github.spinkle.service;

import com.ddojai.github.spinkle.domain.message.Message;
import com.ddojai.github.spinkle.domain.message.MessageRepository;
import com.ddojai.github.spinkle.domain.money.Money;
import com.ddojai.github.spinkle.domain.money.MoneyRepository;
import com.ddojai.github.spinkle.domain.room.Room;
import com.ddojai.github.spinkle.domain.user.User;
import com.ddojai.github.spinkle.domain.user.UserRepository;
import com.ddojai.github.spinkle.dto.MoneySendRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageService {

  private final MessageRepository messageRepository;
  private final MoneyRepository moneyRepository;
  private final UserRepository userRepository;

  @Transactional
  public void sendMoney(Long userId, Room room, String token, MoneySendRequestDto requestDto) {
    int sendAmount = requestDto.getSendAmount();
    int numberPeople = requestDto.getNumberPeople();

    // user
    User user = userRepository.findById(userId)
      .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 입니다. " + userId));
    if (sendAmount > user.getMoneyAmount()) {
      throw new IllegalArgumentException("돈이 부족합니다.");
    }
    user.update(user.getMoneyAmount() -  sendAmount);

    // message
    Message message = messageRepository.save(Message.builder()
      .user(user)
      .room(room)
      .token(token)
      .moneyAmount(sendAmount)
      .build());

    // money
    for (int i = 0; i < numberPeople; i++) {
      int moneyAmount = sendAmount / (numberPeople - i);
      sendAmount -= moneyAmount;
      moneyRepository.save(Money.builder()
        .message(message)
        .receivedUserId(0L)
        .moneyAmount(moneyAmount)
        .build());
    }
  }

  @Transactional(readOnly = true)
  public List<Message> findAllByRoom(Room room) {
    return messageRepository.findAllByRoom(room);
  }

  @Transactional(readOnly = true)
  public Message findByRoomAndToken(Room room, String token) {
    return messageRepository.findByRoomAndToken(room, token)
      .orElseThrow(() -> new IllegalArgumentException("메시지가 존재하지 않습니다."));
  }
}
