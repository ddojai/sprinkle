package com.ddojai.github.spinkle.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MoneyReceiveResponseDto {

  private int receivedAmount;

  @Builder
  public MoneyReceiveResponseDto(int receivedAmount) {
    this.receivedAmount = receivedAmount;
  }
}
