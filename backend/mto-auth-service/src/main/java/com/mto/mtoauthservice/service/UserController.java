package com.mto.mtoauthservice.service;

import com.mto.mtoauthservice.model.User;
import com.mto.mtoauthservice.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public Mono<User> getUser(
            @PathVariable String id) {
        System.out.println("MTO AUTH getUser called with id "+id);
        return userRepository.findById(id);
    }
}
