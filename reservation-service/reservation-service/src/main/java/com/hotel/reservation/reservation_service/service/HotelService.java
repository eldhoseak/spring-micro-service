package com.hotel.reservation.reservation_service.service;

import com.hotel.reservation.reservation_service.model.HotelRoom;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import reactor.core.publisher.Mono;

@Service
public class HotelService {

    private final WebClient webClient;

    public HotelService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:9003").build();
    }

    @CircuitBreaker(name = "hotelServiceCircuitBreaker", fallbackMethod = "fallbackGetRooms")
    public Mono<HotelRoom> getRoom(Long roomId) {
        return webClient.get()
                .uri("/rooms/{roomId}", roomId)
                .retrieve()
                .bodyToMono(HotelRoom.class);
    }

    public Mono<HotelRoom>fallbackGetRooms(Throwable t) {
        HotelRoom fallbackRoom = new HotelRoom();
        fallbackRoom.setRoomId((long) -1);
        return Mono.just(fallbackRoom);
    }
}