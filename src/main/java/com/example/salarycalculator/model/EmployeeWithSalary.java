package com.example.salarycalculator.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EmployeeWithSalary extends Employee {
    private Double salary;
}
