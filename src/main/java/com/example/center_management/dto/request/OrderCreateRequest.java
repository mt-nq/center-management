// OrderCreateRequest.java
package com.example.center_management.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderCreateRequest {

    private Integer courseId;
    private BigDecimal amount;
}
