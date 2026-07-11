package com.mto.madetoorder.backend.resolver;

import com.mto.madetoorder.backend.model.Menu;
import com.mto.madetoorder.backend.repository.MenuRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
public class MenuResolver {

    private final MenuRepository menuRepository;
    private final ReactiveMongoTemplate mongoTemplate;

    public MenuResolver(MenuRepository menuRepository, ReactiveMongoTemplate mongoTemplate) {
        this.menuRepository = menuRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @QueryMapping
    public Flux<Menu> menuItems(@Argument String kitchenId) {
        Flux<Menu> list = menuRepository.findByKitchenId(kitchenId);
        System.out.println(" Menu() called, found: " + list.count());
        System.out.println(" DB: " + mongoTemplate.getMongoDatabase().block().getName());

        return list;
    }
}

