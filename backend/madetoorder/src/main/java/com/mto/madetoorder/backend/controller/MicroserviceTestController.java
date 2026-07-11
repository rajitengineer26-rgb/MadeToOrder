package com.mto.madetoorder.backend.controller;

import com.mto.madetoorder.backend.dto.UserDto;
import com.mto.madetoorder.backend.service.AuthClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/test")
public class MicroserviceTestController {

    private final AuthClient authClient;

    public MicroserviceTestController(AuthClient authClient) {
        this.authClient = authClient;
    }

    @GetMapping("/user/{id}")
    public Mono<UserDto> getUser(
            @PathVariable String id) {

        return authClient.getUser(id);
    }
}