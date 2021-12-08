package com.example.demo.controller;

import com.example.demo.dto.MessageDto;
import com.example.demo.dto.NotificationDto;
import com.example.demo.dto.RoomDto;
import com.example.demo.models.Message;
import com.example.demo.models.Room;
import com.example.demo.models.User;
import com.example.demo.repo.MessageRepo;
import com.example.demo.repo.RoomRepo;
import com.example.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private RoomRepo roomRepo;

    @MessageMapping("/sendAll")
//    @SendTo("/myTopic/messages")
    public MessageDto getMessages(SimpMessageHeaderAccessor sha, Principal user, @Payload MessageDto dto) {
        System.out.println(user.getName());
        System.out.println(sha.getSessionId());
        System.out.println(dto);

        simpMessagingTemplate.convertAndSend("/topic/messages", dto);
        return dto;
    }

    @MessageMapping("/sendMessage")
    @Transactional
    public void sendSpecific(@Payload MessageDto messageDto,
                             Principal principal,
                             @Header("simpSessionId") String sessionId) throws Exception {

        User user = userRepo.findByUsername(principal.getName()).get();

        System.out.println(messageDto);

        Message message = new Message();
        message.setDate(new Date().getTime());
        message.setText(messageDto.getMessage());
        message.setOwner(user);

        System.out.println(messageDto.getRoomId());

        Room room = roomRepo.findById(messageDto.getRoomId()).get();
        List<Long> usersId = room.getUsers().stream().map(User::getId).collect(Collectors.toList());
        message.setRoom(room);
        message = messageRepo.save(message);

        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setType("GET_MESSAGE");
        notificationDto.setMessage(messageDto.getMessage());
        notificationDto.setMessageId(message.getId());
        notificationDto.setDate(message.getDate());
        notificationDto.setRoomId(room.getId());
        notificationDto.setOwnerId(user.getId());



        for (int i = 0; i < usersId.size(); i++) {
            System.out.println(usersId.get(i).toString());
            simpMessagingTemplate.convertAndSendToUser(
                    usersId.get(i).toString(), "/sendMessage/notification", notificationDto);
        }
    }


    @MessageMapping("/createRoom")
    public void createRoom(@Payload RoomDto roomDto,
                           Principal principal,
                           @Header("simpSessionId") String sessionId) throws Exception {

        System.out.println(roomDto);

        User user = userRepo.findByUsername(principal.getName()).get();

        System.out.println(user.getUsername());

        List<User> users = userRepo.findAllById(roomDto.getUsersId());
        users.add(user);

        Room room = new Room();
        room.setDateCreated(new Date().getTime());
        room.setUsers(users);
        room.setOwner(user);
        room.setType("GROUP_CHAT");

        NotificationDto notificationDto = new NotificationDto();

        if (roomDto.getMessage() != null) {
            Message message = new Message();
            message.setDate(new Date().getTime());
            message.setText(roomDto.getMessage());
            message.setOwner(user);
//            message.setRoom(room);
            message = messageRepo.save(message);

            room.getMessages().add(message);
            notificationDto.setMessageId(message.getId());
        }

        room = roomRepo.save(room);

        notificationDto.setType("CREATE_GROUP_CHAT");
        notificationDto.setOwnerId(roomDto.getOwnerId());
        notificationDto.setRoomId(room.getId());
        notificationDto.setDate(room.getDateCreated());
//        notificationDto.setRoom(room);
        if (roomDto.getMessage() != null) notificationDto.setMessage(roomDto.getMessage());


        for (int i = 0; i < users.size(); i++) {
            System.out.println(users.get(i).getId().toString());
            simpMessagingTemplate.convertAndSendToUser(
                    users.get(i).getId().toString(), "/createRoom/notification", notificationDto);
        }
    }
}
