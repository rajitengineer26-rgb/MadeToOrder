package com.mto.madetoorder.backend.service;

import com.mto.madetoorder.backend.dto.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthClient {

    private final WebClient authWebClient;

    public AuthClient(WebClient webClient) {
        this.authWebClient = webClient;
    }


    public Mono<UserDto> getUser(String userId) {

        return authWebClient
                .get()
                .uri("/users/{id}", userId)
                .retrieve()
                .bodyToMono(UserDto.class)
                .doOnNext(user ->
                        System.out.println(
                                "Received user from Auth Service: "
                                        + user.getPhoneNumber()
                        )
                );
    }

}
