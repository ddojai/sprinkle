package com.ddojai.github.spinkle.service;

import com.ddojai.github.spinkle.domain.money.Money;
import com.ddojai.github.spinkle.domain.user.User;
import com.ddojai.github.spinkle.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;

  @Transactional
  public User save(String name, int moneyAmount) {
    return userRepository.save(User.builder()
      .name(name)
      .moneyAmount(moneyAmount)
      .build());
  }

  @Transactional
  public int receiveMoney(Long id, Money money) {
    User user = this.findById(id);
    user.update(user.getMoneyAmount() + money.getMoneyAmount());

    money.update(id);

    return money.getMoneyAmount();
  }

  @Transactional(readOnly = true)
  public User findById(Long id) {
    return userRepository.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 입니다. "+ id));
  }
}
