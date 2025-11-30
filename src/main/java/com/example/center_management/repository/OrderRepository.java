package com.example.center_management.repository;

import com.example.center_management.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Lấy đơn chờ duyệt
    List<Order> findByApprovalStatus(String approvalStatus);

    // Lấy đơn theo học viên (nếu cần)
    List<Order> findByStudent_Id(Long studentId);
}
