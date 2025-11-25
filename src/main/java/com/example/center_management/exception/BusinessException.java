package com.example.center_management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Tùy chọn: Gán mã trạng thái HTTP 400 (Bad Request) nếu exception này được ném ra từ Controller
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessException extends RuntimeException {

    // Constructor đơn giản với thông báo lỗi
    public BusinessException(String message) {
        super(message);
    }

    // Constructor với thông báo lỗi và nguyên nhân (cause)
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}