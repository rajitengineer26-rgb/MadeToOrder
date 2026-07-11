package com.mto.mtoauthservice.service;

import com.mto.mtoauthservice.model.SendOtpRequest;
import com.mto.mtoauthservice.model.User;
import com.mto.mtoauthservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final Random random = new Random();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Mono<Map<String, Object>> sendOtp(SendOtpRequest request) {
        String phoneNumber = request.getPhoneNumber();
        System.out.println("sendOtp called for number " + phoneNumber);
        String code = String.format("%06d", random.nextInt(999999));
        System.out.println("OTP for " + phoneNumber + ": " + code);

        return userRepository.findByPhoneNumber(phoneNumber).flatMap(existingUser ->
                {
                    System.out.println("existing user found");
                    User.Otp otp = new User.Otp();
                    otp.setCode(code);
                    otp.setExpiresAt(Instant.now().plus(5, ChronoUnit.MINUTES));
                    otp.setVerified(false);
                    existingUser.setOtp(otp);
                    return userRepository.save(existingUser);
                }
        ).switchIfEmpty(

                userRepository.save(createNewUser(phoneNumber, code)).doOnSuccess(user -> {
                    assert user != null;
                    System.out.println("saved user id " + user.getId());
                }).doOnError(error -> {
                    System.out.println("Error while saving user " + error.getMessage());
                })

        ).map(savedUser -> {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "OTP sent successfully");
            response.put("expiresIn", 300);
            return response;
        });
    }

    private User createNewUser(String phoneNumber, String code) {

        User user = new User();

        user.setPhoneNumber(phoneNumber);
        user.setRole(User.UserRole.CUSTOMER);
        user.setActive(true);
        user.setCreatedAt(Instant.now());

        User.Otp otp = new User.Otp();
        otp.setCode(code);
        otp.setExpiresAt(Instant.now().plus(5, ChronoUnit.MINUTES));
        otp.setVerified(false);

        user.setOtp(otp);

        return user;
    }
}