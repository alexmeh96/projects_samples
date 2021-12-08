package com.example.demo.controller;

import com.example.demo.models.Room;
import com.example.demo.models.User;
import com.example.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8082", allowCredentials = "true")
@RestController
public class MainController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public String mainPage() {
        return "Main page";
    }

    @GetMapping("/public")
    public String publicPage() {
        return "Public page";
    }

    @GetMapping("/private")
    public String privatePage(Principal principal) {
        String username = principal.getName();
        System.out.println(username);
        return "Private page: user - " + username;
    }

    @GetMapping("/get-users")
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(userRepo.findAll());
    }

    @GetMapping("/get-user-rooms")
    public ResponseEntity<?> getUserRooms(Principal principal) {
        List<Room> rooms = userRepo.findByUsername(principal.getName()).get().getRooms();
        return ResponseEntity.ok(rooms);
    }



}