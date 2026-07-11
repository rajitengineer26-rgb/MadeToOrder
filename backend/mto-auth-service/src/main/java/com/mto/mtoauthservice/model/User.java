package com.mto.mtoauthservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String phoneNumber;
    private String email;
    private UserRole role;

    private boolean isActive;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant lastLoginAt;

    // ✅ Better OTP structure
    private Otp otp;

    // ✅ Prepare for future (but lightweight)
    private Profile profile;

    public enum UserRole {
        CUSTOMER, KITCHEN, ADMIN
    }

    @Data
    public static class Otp {
        private String code;
        private Instant expiresAt;
        private boolean verified;
    }

    @Data
    public static class Profile {
        private String firstName;
        private String lastName;
        private String profileImageUrl;
    }
}
