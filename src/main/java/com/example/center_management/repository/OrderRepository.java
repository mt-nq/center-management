package com.example.center_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.center_management.domain.entity.Order;
import com.example.center_management.domain.enums.ApprovalStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Lấy đơn chờ duyệt
    List<Order> findByApprovalStatus(ApprovalStatus approvalStatus);

    // Lấy đơn theo học viên (nếu cần)
    List<Order> findByStudent_Id(Long studentId);

    List<Order> findByStudentIdAndCourseId(Long studentId, Long courseId);

}
