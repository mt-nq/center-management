package com.example.center_management.dto.response;

import lombok.Data;

@Data
public class PaymentInfoResponse {

    private String bankAccountNumber;
    private String bankAccountName;
    private String bankName;
    private String transferGuide;
}
