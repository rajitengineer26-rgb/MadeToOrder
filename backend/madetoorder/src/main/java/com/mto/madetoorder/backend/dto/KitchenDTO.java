package com.mto.madetoorder.backend.dto;

import lombok.Data;

@Data
public class KitchenDTO {
    private String id;
    private String name;
    private String area;
    private String city;
    private String imageUrl;
    private Double rating; // flattened for UI
}