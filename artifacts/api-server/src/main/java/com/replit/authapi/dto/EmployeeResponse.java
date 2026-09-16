package com.replit.authapi.dto;

import com.replit.authapi.model.Employee;

public record EmployeeResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String department
) {
    public static EmployeeResponse from(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getDepartment()
        );
    }
}