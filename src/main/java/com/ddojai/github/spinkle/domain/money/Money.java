package com.ddojai.github.spinkle.domain.money;

import com.ddojai.github.spinkle.domain.BaseTimeEntity;
import com.ddojai.github.spinkle.domain.message.Message;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Money extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "money_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "message_id")
  private Message message;

  @ColumnDefault("0")
  private Long receivedUserId;

  private int moneyAmount;

  @Builder
  public Money(Message message, Long receivedUserId, int moneyAmount) {
    this.message = message;
    this.receivedUserId = receivedUserId;
    this.moneyAmount = moneyAmount;
  }

  public void update(Long receivedUserId) {
    this.receivedUserId = receivedUserId;
  }
}
