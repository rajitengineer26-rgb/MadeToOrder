package com.mto.madetoorder.backend.repository;

import com.mto.madetoorder.backend.model.Kitchen;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Collection;

@Repository
public interface KitchenRepository extends ReactiveMongoRepository<Kitchen, String> {
    Flux<Kitchen> findByTypeIgnoreCase(String type);

    Flux<Kitchen> findByIdIn(Collection<String> ids);
}


