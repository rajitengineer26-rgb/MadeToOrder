package com.mto.madetoorder.backend.repository;

import com.mto.madetoorder.backend.model.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface MenuRepository extends ReactiveMongoRepository<Menu, String> {
    Flux<Menu> findByKitchenId(String kitchenId);

    Flux<Menu> findByNameContainingIgnoreCase(String name);
}


