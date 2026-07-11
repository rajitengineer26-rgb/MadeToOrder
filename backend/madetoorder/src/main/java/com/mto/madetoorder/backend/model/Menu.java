package com.mto.madetoorder.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "menus")
public class Menu {

    @Id
    private String id;
    private String kitchenId;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private String category;
    private String type;
}
