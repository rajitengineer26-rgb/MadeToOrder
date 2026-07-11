package com.mto.madetoorder.backend.controller;

import com.mto.madetoorder.backend.model.SendOtpRequest;
import com.mto.madetoorder.backend.repository.UserRepository;
import com.mto.madetoorder.backend.security.JwtUtil;
import com.mto.madetoorder.backend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthService authService;

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil, AuthService authService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @PostMapping("/otp/send")
    public Mono<ResponseEntity<?>> sendOtp(@RequestBody SendOtpRequest request) {
        return authService.sendOtp(request).map(ResponseEntity::ok);
    }

    @PostMapping("/otp/verify")
    public Mono<ResponseEntity<?>> verifyOtp(
            @RequestBody Map<String, String> request) {

        String phoneNumber = request.get("phoneNumber");
        String otp = request.get("otp");

        return userRepository.findByPhoneNumber(phoneNumber)

                .switchIfEmpty(Mono.error(
                        new RuntimeException("USER_NOT_FOUND")))

                .flatMap(user -> {

                    String otpCode = user.getOtp().getCode();

                    if (otpCode == null || !otpCode.equals(otp)) {
                        return errorResponse(
                                "INVALID_OTP",
                                "Invalid OTP"
                        );
                    }

                    if (user.getOtp().getExpiresAt()
                            .isBefore(Instant.now())) {

                        return errorResponse(
                                "OTP_EXPIRED",
                                "OTP has expired"
                        );
                    }

                    user.getOtp().setVerified(true);
                    user.setLastLoginAt(Instant.now());

                    return userRepository.save(user)
                            .map(savedUser -> {

                                String accessToken =
                                        jwtUtil.generateToken(
                                                savedUser.getId(),
                                                savedUser.getPhoneNumber(),
                                                savedUser.getRole().name());

                                String refreshToken =
                                        jwtUtil.generateRefreshToken(
                                                savedUser.getId());

                                Map<String, Object> response =
                                        new HashMap<>();

                                response.put("success", true);

                                Map<String, Object> data =
                                        new HashMap<>();

                                data.put("accessToken", accessToken);
                                data.put("refreshToken", refreshToken);
                                data.put("expiresIn", 86400);

                                Map<String, Object> userData =
                                        new HashMap<>();

                                userData.put("id",
                                        savedUser.getId());

                                userData.put("phoneNumber",
                                        savedUser.getPhoneNumber());

                                userData.put("role",
                                        savedUser.getRole().name());

                                userData.put(
                                        "isProfileComplete",
                                        savedUser.getEmail() != null
                                                && !savedUser.getEmail().isEmpty()
                                );

                                data.put("user", userData);
                                response.put("data", data);

                                return ResponseEntity.ok(response);
                            });
                })

                .onErrorResume(ex -> {
                    if ("USER_NOT_FOUND".equals(ex.getMessage())) {
                        return errorResponse(
                                "USER_NOT_FOUND",
                                "User not found"
                        );
                    }

                    return Mono.just(
                            ResponseEntity.badRequest()
                                    .body(Map.of(
                                            "error",
                                            ex.getMessage()
                                    ))
                    );
                });
    }

    private Mono<ResponseEntity<?>> errorResponse(String code, String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        Map<String, Object> errorData = new HashMap<>();
        errorData.put("code", code);
        errorData.put("message", message);
        error.put("error", errorData);
        return Mono.just(ResponseEntity.badRequest().body(error));
    }

    @PostMapping("/refresh")
    public Mono<ResponseEntity<?>> refreshToken(
            @RequestBody Map<String, String> request) {

        String refreshToken = request.get("refreshToken");

        try {

            if (!jwtUtil.validateToken(refreshToken)) {
                return Mono.just(
                        ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(Map.of(
                                        "error",
                                        "Invalid refresh token"
                                ))
                );
            }

            String tokenType =
                    jwtUtil.getClaimFromToken(
                            refreshToken,
                            "tokenType"
                    ).toString();

            if (!"REFRESH".equals(tokenType)) {
                return Mono.just(
                        ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(Map.of(
                                        "error",
                                        "Invalid token type"
                                ))
                );
            }

            String userId =
                    jwtUtil.getUserIdFromToken(refreshToken);

            return userRepository.findById(userId)

                    .map(user -> {

                        String newAccessToken =
                                jwtUtil.generateToken(
                                        user.getId(),
                                        user.getPhoneNumber(),
                                        user.getRole().name()
                                );

                        return ResponseEntity.ok(
                                Map.of(
                                        "accessToken",
                                        newAccessToken
                                )
                        );
                    });
        } catch (Exception e) {

            return Mono.just(
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(Map.of(
                                    "error",
                                    "Token expired or invalid"
                            ))
            );
        }
    }
}
