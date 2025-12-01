package com.example.center_management.repository;

import com.example.center_management.domain.entity.Order;
import com.example.center_management.domain.enums.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByStudent_Id(Integer studentId);

    List<Order> findByApprovalStatus(ApprovalStatus status);
}
