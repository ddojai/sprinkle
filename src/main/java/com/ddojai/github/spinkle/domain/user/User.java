package com.ddojai.github.spinkle.domain.user;

import com.ddojai.github.spinkle.domain.BaseTimeEntity;
import com.ddojai.github.spinkle.domain.roomuser.RoomUser;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @Column(length = 64, nullable = false, unique = true)
  private String name;

  private int moneyAmount;

  @OneToMany(mappedBy = "user")
  private final List<RoomUser> roomUsers = new ArrayList<>();

  @Builder
  public User(String name, int moneyAmount) {
    this.name = name;
    this.moneyAmount = moneyAmount;
  }

  public void update(int moneyAmount) {
    this.moneyAmount = moneyAmount;
  }
}
