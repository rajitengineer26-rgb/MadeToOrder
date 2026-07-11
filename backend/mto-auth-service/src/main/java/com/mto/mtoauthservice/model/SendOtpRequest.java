package com.mto.mtoauthservice.model;

import lombok.Data;

@Data
public class SendOtpRequest {
    private String phoneNumber;
    private String purpose;
}