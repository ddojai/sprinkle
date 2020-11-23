package com.ddojai.github.spinkle.service;

import com.ddojai.github.spinkle.domain.room.Room;
import com.ddojai.github.spinkle.domain.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RoomService {

  private final RoomRepository roomRepository;

  @Transactional
  public Room save() {
    String code = UUID.randomUUID().toString();

    return roomRepository.save(Room.builder()
      .code(code)
      .build());
  }

  @Transactional(readOnly = true)
  public Room findByCode(String code) {
    return roomRepository.findByCode(code)
      .orElseThrow(() -> new IllegalArgumentException("존재하는 방이 아닙니다."+ code));
  }
}
