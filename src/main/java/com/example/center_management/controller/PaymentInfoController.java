package com.example.center_management.controller;

import com.example.center_management.dto.response.PaymentInfoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentInfoController {

    @GetMapping("/payment-info")
    public ResponseEntity<PaymentInfoResponse> getPaymentInfo() {
        PaymentInfoResponse res = new PaymentInfoResponse();
        res.setBankAccountNumber("123456789");
        res.setBankAccountName("TRUNG TAM DAO TAO ABC");
        res.setBankName("Vietcombank - CN Ha Noi");
        res.setTransferGuide("Nội dung: SDT - HO TEN - TEN KHOA HOC");

        return ResponseEntity.ok(res);
    }
}
