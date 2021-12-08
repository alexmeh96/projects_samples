package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private String message;
    private Long senderId;
    private Long roomId;
//    private List<Long> receiversId = new ArrayList<>();

    public MessageDto(String message) {
        this.message = message;
    }
}
