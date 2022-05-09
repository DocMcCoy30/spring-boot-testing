package com.dmcdev.springboot.service.contract;

import com.dmcdev.springboot.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
//    Employee getEmployeeById(Long id);
    Optional<Employee> getEmployeeById(Long id);
    Employee updateEmployee(Employee employeeToUpdate);
    void deleteEmploye(Long id);
}
