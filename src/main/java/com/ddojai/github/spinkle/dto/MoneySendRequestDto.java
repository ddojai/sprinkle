package com.ddojai.github.spinkle.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MoneySendRequestDto {

  private int sendAmount;
  private int numberPeople;

  @Builder
  public MoneySendRequestDto(int sendAmount, int numberPeople) {
    this.sendAmount = sendAmount;
    this.numberPeople = numberPeople;
  }
}
