package com.ddojai.github.spinkle.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MoneySendResponseDto {

  private String token;

  public MoneySendResponseDto(String token) {
    this.token = token;
  }
}
