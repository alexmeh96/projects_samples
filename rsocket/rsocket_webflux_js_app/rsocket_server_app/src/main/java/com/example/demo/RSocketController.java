package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Controller
class RSocketController {

    @Autowired
    private CarService carService;

    @ConnectMapping
    void connectClient(RSocketRequester requester, @Headers Map<String, Object> obj, @Payload(required = false) String data) {

        requester.rsocket()
                .onClose()
                .doFirst(() -> {
                    log.info("Client CONNECTED");
                })
                .doOnError(error -> {
                    log.warn("Channel to client CLOSED");
                })
                .doFinally(consumer -> {
                    log.info("Client DISCONNECTED");
                })
                .subscribe();
    }

    @MessageMapping("car/stream")
    public Flux<Car> stream() {
        return carService.streamOfCars();
    }

    @MessageMapping("car/request-response")
    Mono<Car> requestResponse(RSocketRequester requester, @Headers Map<String, Object> obj, Car car) {
        System.out.println(requester);
        System.out.println("Received request-response request: " + car);
        return Mono.just(new Car("car_" + (int) (Math.random() * 100), (long) (Math.random() * 1000000)));
    }

    @MessageMapping("car/fire-and-forget")
    public void fireAndForget(Car car) {
        log.info("Received fire-and-forget request: {}", car);
    }

    @MessageMapping("channel")
    Flux<Car> channel(final Flux<?> numbers) {
        numbers.subscribe(System.out::println);
        return carService.streamOfCars();
    }

}