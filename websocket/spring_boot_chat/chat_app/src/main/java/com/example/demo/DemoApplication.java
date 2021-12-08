package com.example.demo;

import com.example.demo.models.User;
import com.example.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Autowired
    UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
//        List<User> users = List.of(
//                new User("user1", passwordEncoder.encode("password")),
//                new User("user2", passwordEncoder.encode("password")),
//                new User("user3", passwordEncoder.encode("password")),
//                new User("user4", passwordEncoder.encode("password")),
//                new User("user5", passwordEncoder.encode("password"))
//        );
//
//        userRepo.saveAll(users);

    }
}
