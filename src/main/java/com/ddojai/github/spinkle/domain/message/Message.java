package com.ddojai.github.spinkle.domain.message;

import com.ddojai.github.spinkle.domain.BaseTimeEntity;
import com.ddojai.github.spinkle.domain.money.Money;
import com.ddojai.github.spinkle.domain.room.Room;
import com.ddojai.github.spinkle.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@NoArgsConstructor
@Entity
@Table(
  uniqueConstraints = {
    @UniqueConstraint(
      columnNames = {"room_id", "token"}
    )
  }
)
public class Message extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "message_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "room_id")
  private Room room;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(length = 3, nullable = false)
  private String token;

  private int moneyAmount;

  @OneToMany(mappedBy = "message")
  private final List<Money> moneyList = new ArrayList<>();

  @Builder
  public Message(Room room, User user, String token, int moneyAmount) {
    this.room = room;
    this.user = user;
    this.token = token;
    this.moneyAmount = moneyAmount;
  }

  public static String generateToken(List<String> existTokens) {
    String token;
    StringBuilder sb = new StringBuilder();
    Random random = new Random();
    do {
      for (int i = 0; i < 3; i++) {
        switch (random.nextInt(3)) {
          case 0:
            // a-z
            sb.append((char) (random.nextInt(26) + 97));
            break;
          case 1:
            // A-Z
            sb.append((char) (random.nextInt(26) + 65));
            break;
          case 2:
            // 0-9
            sb.append((random.nextInt(10)));
            break;
        }
      }
      token = sb.toString();
      sb.setLength(0);
    } while (existTokens.contains(token));


    return token;
  }
}
