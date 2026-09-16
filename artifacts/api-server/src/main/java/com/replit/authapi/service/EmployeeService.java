package com.replit.authapi.service;

import com.replit.authapi.dto.EmployeeRequest;
import com.replit.authapi.dto.EmployeeResponse;
import com.replit.authapi.model.Employee;
import com.replit.authapi.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Transactional
    public EmployeeResponse create(EmployeeRequest request) {
        Employee employee = new Employee(
                request.firstName().trim(),
                request.lastName().trim(),
                request.email().trim().toLowerCase(),
                request.department().trim()
        );
        return EmployeeResponse.from(employeeRepository.save(employee));
    }
}