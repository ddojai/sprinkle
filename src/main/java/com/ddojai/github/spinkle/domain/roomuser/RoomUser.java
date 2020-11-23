package com.ddojai.github.spinkle.domain.roomuser;

import com.ddojai.github.spinkle.domain.BaseTimeEntity;
import com.ddojai.github.spinkle.domain.room.Room;
import com.ddojai.github.spinkle.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class RoomUser extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "room_user_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "room_id")
  private Room room;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Builder
  public RoomUser(Room room, User user) {
    this.room = room;
    this.user = user;
  }
}
