package com.example.demo.repo;

import com.example.demo.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoomRepo extends JpaRepository<Room, Long> {
    @Override
//    @Query("SELECT DISTINCT room FROM Room room " +
//            "JOIN FETCH room.users users")
    Optional<Room> findById(Long aLong);
}
