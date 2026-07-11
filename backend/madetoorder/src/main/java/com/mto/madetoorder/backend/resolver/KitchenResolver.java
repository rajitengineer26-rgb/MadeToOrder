package com.mto.madetoorder.backend.resolver;

import com.mto.madetoorder.backend.dto.KitchenDTO;
import com.mto.madetoorder.backend.exception.KitchenNotFoundException;
import com.mto.madetoorder.backend.mapper.KitchenMapper;
import com.mto.madetoorder.backend.model.Kitchen;
import com.mto.madetoorder.backend.model.KitchenFilter;
import com.mto.madetoorder.backend.model.Menu;
import com.mto.madetoorder.backend.model.PaginationInput;
import com.mto.madetoorder.backend.repository.KitchenRepository;
import com.mto.madetoorder.backend.repository.MenuRepository;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Controller
public class KitchenResolver {

    private final KitchenRepository kitchenRepository;
    private final MenuRepository menuRepository;
    private final KitchenMapper kitchenMapper;

    public KitchenResolver(KitchenRepository kitchenRepository,
                           MenuRepository menuRepository,
                           KitchenMapper kitchenMapper) {
        this.kitchenRepository = kitchenRepository;
        this.menuRepository = menuRepository;
        this.kitchenMapper = kitchenMapper;
    }

    @QueryMapping
    public Flux<KitchenDTO> kitchens(
            @Argument @Valid KitchenFilter filter,
            @Argument String search,
            @Argument @Valid PaginationInput pagination) {

        int page = (pagination != null) ? pagination.getPage() : 0;
        int size = (pagination != null) ? pagination.getSize() : 10;

        // ✅ STEP 1: BASE STREAM
        Flux<Kitchen> kitchens;

        if (search != null && !search.isEmpty()) {

            kitchens = menuRepository.findByNameContainingIgnoreCase(search)
                    .parallel()
                    .runOn(Schedulers.boundedElastic())
                    .map(Menu::getKitchenId)
                    .sequential()
                    .distinct()
                    .flatMap(kitchenRepository::findById);

        } else {
            kitchens = kitchenRepository.findAll();
        }

        // ✅ STEP 2: APPLY FILTER (CLEAN + SAFE)
        if (filter != null && filter.getType() != null) {
            kitchens = kitchenRepository.findByTypeIgnoreCase(filter.getType());
        }

        Mono<Long> totalCount = kitchens.count();

        // ✅ STEP 3: PAGINATION (Reactive)
        kitchens = kitchens
                .skip((long) page * size)
                .take(size);

        // ✅ STEP 4: ERROR + DTO MAPPING
        return kitchens
                .switchIfEmpty(Flux.error(new KitchenNotFoundException("No kitchen found")))
                .map(kitchenMapper::toDTO);
    }

    @QueryMapping
    public Flux<Kitchen> kitchen(@Argument String id) {
        return kitchenRepository.findById(id).flux();
    }
}
