package com.mto.madetoorder.backend.model;

import lombok.Data;

@Data
public class SendOtpRequest {
    private String phoneNumber;
    private String purpose;
}