package com.ddojai.github.spinkle.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MoneyCheckResponseDto {

  private final LocalDateTime sendDate;
  private final int totalAmount;
  private final int receivedCompletedAmount;
  private final List<ReceivedUserDto> receivedUserDtos;

  @Builder
  public MoneyCheckResponseDto(LocalDateTime sendDate, int totalAmount,
                               int receivedCompletedAmount,
                               List<ReceivedUserDto> receivedUserDtos) {
    this.sendDate = sendDate;
    this.totalAmount = totalAmount;
    this.receivedCompletedAmount = receivedCompletedAmount;
    this.receivedUserDtos = receivedUserDtos;
  }
}