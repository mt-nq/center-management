package com.example.center_management.controller.admin;

import com.example.center_management.dto.request.StudentUpdateRequest;
import com.example.center_management.dto.response.StudentResponse;
import com.example.center_management.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/students")
@RequiredArgsConstructor
public class AdminStudentController {

    private final StudentService studentService;

    // GET /api/admin/students?page=0&size=10
    @GetMapping
    public ResponseEntity<Page<StudentResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<StudentResponse> res = studentService.getAll(page, size);
        return ResponseEntity.ok(res);
    }

    // GET /api/admin/students/{id}
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getById(@PathVariable("id") Long id) {
        System.out.println(">>> GET student id = " + id);
        StudentResponse res = studentService.getById(id);
        return ResponseEntity.ok(res);
    }

    // DELETE /api/admin/students/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        System.out.println(">>> DELETE student id = " + id);
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
