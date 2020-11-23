package com.ddojai.github.spinkle.service;

import com.ddojai.github.spinkle.domain.room.Room;
import com.ddojai.github.spinkle.domain.roomuser.RoomUser;
import com.ddojai.github.spinkle.domain.roomuser.RoomUserRepository;
import com.ddojai.github.spinkle.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RoomUserService {

  private final RoomUserRepository roomUserRepository;

  @Transactional
  public void save(Room room, User user) {
    roomUserRepository.save(RoomUser.builder()
      .room(room)
      .user(user)
      .build());
  }
}
