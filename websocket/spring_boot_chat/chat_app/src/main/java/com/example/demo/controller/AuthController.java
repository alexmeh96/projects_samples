package com.example.demo.controller;

import com.example.demo.dto.ChatDto;
import com.example.demo.models.Room;
import com.example.demo.models.User;
import com.example.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8082", allowCredentials = "true")
@RestController
@RequestMapping("/private")
public class AuthController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/get-chat")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        User currentUser = userRepo.findByUsername(principal.getName()).get();
        List<Room> rooms = currentUser.getRooms();
        List<User> users = userRepo.findAll();

        ChatDto chatDto = new ChatDto(currentUser, rooms, users);

        return ResponseEntity.ok(chatDto);
    }
}
