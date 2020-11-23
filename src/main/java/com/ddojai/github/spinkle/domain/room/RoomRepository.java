package com.ddojai.github.spinkle.domain.room;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
  Optional<Room> findByCode(String code);
}
