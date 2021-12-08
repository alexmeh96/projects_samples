package com.example.demo.dto;

import com.example.demo.models.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private String type;
    private String message;
    private Long messageId;
    private Long date;
    private Long ownerId;
    private Long roomId;
    private Room room;

    public NotificationDto(String message) {
        this.message = message;
    }
}
