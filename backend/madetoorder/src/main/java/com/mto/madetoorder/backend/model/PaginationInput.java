package com.mto.madetoorder.backend.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PaginationInput {
    @Min(0)
    private int page = 0;
    @Min(1)
    @Max(50)
    private int size = 10;
}
