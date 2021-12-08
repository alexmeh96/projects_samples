package com.example.demo.dto;

import com.example.demo.models.Room;
import com.example.demo.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {
    private User currentUser;
    private List<Room> rooms = new ArrayList<>();
    private List<User> users = new ArrayList<>();
}
