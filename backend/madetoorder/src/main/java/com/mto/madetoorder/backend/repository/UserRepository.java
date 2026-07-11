package com.mto.madetoorder.backend.repository;


import com.mto.madetoorder.backend.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findByPhoneNumber(String phoneNumber);

    Mono<Boolean> existsByPhoneNumber(String phoneNumber);
}
