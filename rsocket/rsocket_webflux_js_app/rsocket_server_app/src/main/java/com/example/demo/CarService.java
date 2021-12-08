package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
@Slf4j
class CarService {

    public Flux<Car> streamOfCars() {
        return Flux
                .interval(Duration.ofSeconds(1))
//                .onBackpressureDrop()
                .map(e -> new Car("car_" + (int) (Math.random() * 100), (long) (Math.random() * 1000000)))
                .doOnSubscribe((e) -> log.info("New subscription"))
                .log()
            .share();
    }


}