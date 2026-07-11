package com.mto.madetoorder.backend.dto;

import lombok.Getter;

import java.util.List;

public class KitchenPageResponse {
    @Getter
    private List<KitchenDTO> kitchens;
    @Getter
    private Long total;

    public KitchenPageResponse(List<KitchenDTO> kitchens, Long total) {
        this.kitchens = kitchens;
        this.total = total;
    }

    // getters
}
