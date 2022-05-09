package com.dmcdev.springboot.service.impl;

import com.dmcdev.springboot.exception.ResourceNotFoundException;
import com.dmcdev.springboot.model.Employee;
import com.dmcdev.springboot.repository.EmployeeRepository;
import com.dmcdev.springboot.service.contract.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (savedEmployee.isPresent()) {
            throw new ResourceNotFoundException("Employee already exist with given email: " + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

//    @Override
//    public Employee getEmployeeById(Long id) {
//        Optional<Employee> employeeOptional = employeeRepository.findById(id);
//        if (employeeOptional.isEmpty()) {
//            throw new ResourceNotFoundException("Employee with id " + id + " was not found.");
//        }
//        return employeeOptional.get();
//    }

    @Override
    public Employee updateEmployee(Employee employeeToUpdate) {
        return employeeRepository.save(employeeToUpdate);
    }

    @Override
    public void deleteEmploye(Long id) {
        employeeRepository.deleteById(id);
    }
}
