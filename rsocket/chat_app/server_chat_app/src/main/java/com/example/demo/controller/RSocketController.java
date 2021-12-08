package com.example.demo.controller;

import com.example.demo.Message;
import io.rsocket.util.DefaultPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class RSocketController {

    private final Map<String, RSocketRequester> CLIENTS = new HashMap<>();


    @ConnectMapping
    void connectClient(RSocketRequester requester, @Headers Map<String, Object> obj, @Payload(required = false) String data) {
        System.out.println("connectClientMethod");
        System.out.println(CLIENTS);

        requester.rsocket().requestResponse(DefaultPayload.create("{\"message\":\"Who you are?\"}"))
                .map(payload -> payload.getDataUtf8())
                .doOnNext(e -> {
                    System.out.println(e);
                    CLIENTS.put(e, requester);

                    requester.rsocket()
                            .onClose()
                            .doOnError(error -> {
                                log.warn("Channel to client CLOSED");
                            })
                            .doFinally(consumer -> {
                                CLIENTS.remove(e);
                                log.info("Client {} DISCONNECTED", e);
                                System.out.println(CLIENTS);
                            })
                            .subscribe();
                })
                .subscribe();
    }

    @MessageMapping("chat/fire-and-forget")
    public void chatFireAndForget(Message message) {
        log.info("Received fire-and-forget request: {}", message);
        CLIENTS.get("\"" + message.getSendTo() + "\"").rsocket().fireAndForget(DefaultPayload.create("{\"message\":\""+ message.getMessage() + "\"}")).subscribe();
    }
}
