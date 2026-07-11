package com.mto.madetoorder.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Data
@Document(collection = "kitchens")
public class Kitchen {
    @Id
    private String id;
    private String kitchenName;
    private String slug;
    private String description;
    private String type;
    private String status;
    private Address address;
    private Contact contact;
    private Rating rating;
    private Boolean isOpen;
    private Boolean isTakingOrders;
    private Instant createdAt;
    private Instant updatedAt;
    private GeoJsonPoint location;
    private String imageUrl;

    @Data
    public static class Address {
        private String completeAddress;
        private String area;
        private String city;
        private String state;
        private String pinCode;
    }
        @Data
    public static class Contact {
        private String phone;
        private String email;
    }
    @Data
    public static class Rating {
        private Double average;
        private Integer totalReviews;
    }

}
