package com.mto.madetoorder.backend.model;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class KitchenFilter {

    @Pattern(regexp = "VEGETARIAN|NON_VEGETARIAN|ALL",
            message = "Invalid kitchen type")

    private String type;   // VEGETARIAN / NON_VEGETARIAN

    private String area;
    private String city;
}
