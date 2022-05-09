package com.dmcdev.springboot.service;

import com.dmcdev.springboot.exception.ResourceNotFoundException;
import com.dmcdev.springboot.model.Employee;
import com.dmcdev.springboot.repository.EmployeeRepository;
import com.dmcdev.springboot.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;
    private Employee employee1;

    @BeforeEach
    void setUp() {
//        employeeRepository = Mockito.mock(EmployeeRepository.class);
//        employeeService = new EmployeeServiceImpl(employeeRepository);
        employee1 = Employee.builder()
                .id(1L)
                .firstName("Bernard")
                .lastName("Comolet")
                .email("comolet@mail.com")
                .build();
    }

    @DisplayName("JUnit Test for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        //given
        given(employeeRepository.findByEmail(employee1.getEmail())).willReturn(Optional.empty());
        given(employeeRepository.save(employee1)).willReturn(employee1);
        System.out.println(employeeRepository);
        System.out.println(employeeService);
        //when
        Employee savedEmployee = employeeService.saveEmployee(employee1);
        System.out.println(savedEmployee);
        //then
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("JUnit Test for saveEmployee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException() {
        //given
        given(employeeRepository.findByEmail(employee1.getEmail())).willReturn(Optional.of(employee1));
        //when
        assertThrows(ResourceNotFoundException.class, () -> employeeService.saveEmployee(employee1));
        //then
        verify(employeeRepository, never()).save(employee1);
    }

    @DisplayName("JUnit Test for get all employees method")
    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList() {
        //given
        Employee employee2 = Employee.builder()
                .id(1L)
                .firstName("Bruno")
                .lastName("Francard")
                .email("francard@mail.com")
                .build();
        given(employeeRepository.findAll()).willReturn(List.of(employee1, employee2));
        //when
        List<Employee> employees = employeeService.getAllEmployees();
        //then
        assertThat(employees).isNotNull();
        assertThat(employees.size()).isEqualTo(2);
    }

    @DisplayName("JUnit Test for get all employees method (negative scenario)")
    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList() {
        //given
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());
        //when
        List<Employee> employees = employeeService.getAllEmployees();
        //then
        assertThat(employees).isEmpty();
        assertThat(employees.size()).isEqualTo(0);
    }

    @DisplayName("JUnit Test for get employee by its id method")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {
        //given
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee1));
        //when
        Employee employee = employeeService.getEmployeeById(employee1.getId()).get();
        //then
        assertThat(employee).isNotNull();
        assertThat(employee.getEmail()).isEqualTo("comolet@mail.com");
    }

//    @DisplayName("JUnit Test for get employee by its id method")
//    @Test
//    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {
//        //given
//        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee1));
//        //when
//        Employee employee = employeeService.getEmployeeById(employee1.getId());
//        //then
//        assertThat(employee).isNotNull();
//        assertThat(employee.getEmail()).isEqualTo("comolet@mail.com");
//    }
//
//    @DisplayName("JUnit Test for get employee by its id method which return an exception")
//    @Test
//    public void givenNonExistingId_whenGetEmployeeById_thenReturnReourceNotFoundException() {
//        //given
//        given(employeeRepository.findById(anyLong())).willReturn(Optional.empty());
//        Long id = 1L;
//        //when
//        Exception e = assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployeeById(id));
//        //then
//        assertThat(e.getMessage()).contains(id.toString());
//    }

    @DisplayName("JUnit Test for update employee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        //given
        given(employeeRepository.save(employee1)).willReturn(employee1);
        String updatedEmail = "updatedEmail@mail.com";
        employee1.setEmail(updatedEmail);
        //when
        Employee updatedEmployee = employeeService.updateEmployee(employee1);
        //then
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getEmail()).isEqualTo(updatedEmail);
    }

    @DisplayName("JUnit Test for delete employee by id method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnNothing() {
        //given
        Long employeeId = employee1.getId();
        willDoNothing().given(employeeRepository).deleteById(employeeId);
        //when
        employeeService.deleteEmploye(employeeId);
        //then
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }


}