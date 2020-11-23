package com.ddojai.github.spinkle.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReceivedUserDto {

  private final Long userId;
  private final int receivedAmount;

  @Builder
  public ReceivedUserDto(Long userId, int receivedAmount) {
    this.userId = userId;
    this.receivedAmount = receivedAmount;
  }
}
