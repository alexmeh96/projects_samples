package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {
    private String message;
    private Long ownerId;
    private List<Long> usersId = new ArrayList<>();

    public RoomDto(String message) {
        this.message = message;
    }
}
