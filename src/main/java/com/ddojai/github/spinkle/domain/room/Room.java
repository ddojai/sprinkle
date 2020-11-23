package com.ddojai.github.spinkle.domain.room;

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
public class Room extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "room_id")
  private Long id;

  @Column(length = 36, nullable = false, unique = true)
  private String code;

  @OneToMany(mappedBy = "room")
  private final List<RoomUser> roomUsers = new ArrayList<>();

  @Builder
  public Room(String code) {
    this.code = code;
  }

  public void isUserExist(Long userId) {
    boolean isUserExist = false;
    for (RoomUser roomUser : this.getRoomUsers()) {
      if (roomUser.getUser().getId().equals(userId)) {
        isUserExist = true;
        break;
      }
    }

    if (!isUserExist) {
      throw new IllegalArgumentException("요청한 유저는 해당 채팅방에 없습니다.");
    }
  }
}
