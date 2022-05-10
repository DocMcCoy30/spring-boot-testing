package com.dmcdev.springboot.integration;

import com.dmcdev.springboot.model.Employee;
import com.dmcdev.springboot.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryIT {

    @Autowired
    private EmployeeRepository employeeRepository;
    private Employee employee1;

    @BeforeEach
    void setUp() {
        employee1 = Employee.builder()
                .firstName("Bernard")
                .lastName("Comolet")
                .email("comolet@mail.com")
                .build();
        employeeRepository.deleteAll();
    }

    @DisplayName("JUnit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployeeObject() {
        //given
        //when
        Employee savedEmployee = employeeRepository.save(employee1);
        //then
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @DisplayName("JUnit Test for get all employees operation")
    @Test
    public void givenEmployeesList_whenFindAll_thenReturnEmployeesList() {
        //given
        Employee employee2 = Employee.builder()
                .firstName("Bruno")
                .lastName("Francard")
                .email("francard@gmail.com")
                .build();
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        //when
        List<Employee> employeeList = employeeRepository.findAll();
        //then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @DisplayName("JUnit Test for get an employee by its id operation")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
        //given
        employeeRepository.save(employee1);
        //when
        Employee employeeDB = employeeRepository.findById(employee1.getId()).get();
        //then
        assertThat(employeeDB).isNotNull();
    }

    @DisplayName("JUnit Test for get employee by its email operation")
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {
        //given
        employeeRepository.save(employee1);
        //when
        Employee employeeDB = employeeRepository.findByEmail(employee1.getEmail()).get();
        //then
        assertThat(employeeDB).isNotNull();
    }

    @DisplayName("JUnit Test for update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmploye() {
        //given
        employeeRepository.save(employee1);
        //when
        Employee savedEmployee = employeeRepository.findById(employee1.getId()).get();
        savedEmployee.setEmail("updatedEmail@mail.com");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);
        //then
        assertThat(updatedEmployee.getEmail()).isEqualTo("updatedEmail@mail.com");
    }

    @DisplayName("JUnit Test for delete employee operation")
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployeeObject() {
        //given
        employeeRepository.save(employee1);
        //when
        employeeRepository.delete(employee1);
        Optional<Employee> employeeOptional = employeeRepository.findById(employee1.getId());
        //then
        assertThat(employeeOptional).isEmpty();
    }

    @DisplayName("JUnit Test for delete employee by id operation")
    @Test
    public void givenEmployeeObject_whenDeleteById_thenRemoveEmployeeObject() {
        //given
        employeeRepository.save(employee1);
        //when
        employeeRepository.deleteById(employee1.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee1.getId());
        //then
        assertThat(employeeOptional).isEmpty();
    }

    @DisplayName("JUnit Test for custom query using JPQL with index parameters")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLIndexParams_thenReturnEmployeeObject() {
        //given
        employeeRepository.save(employee1);
        String firstName = "Bernard";
        String lastName = "Comolet";
        //when
        Employee savedEmployee = employeeRepository.findByJPQLIndexParams(firstName, lastName);
        //then
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("JUnit Test for custom query using JPQL with named parameters")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject() {
        //given
        employeeRepository.save(employee1);
        String firstName = "Bernard";
        String lastName = "Comolet";
        //when
        Employee savedEmployee = employeeRepository.findByJPQLNamedParams(firstName, lastName);
        //then
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("JUnit Test for custom query using NativeSQL with index parameters")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQLIndexParams_thenReturnEmployeeObject() {
        //given
        employeeRepository.save(employee1);
        String firstName = "Bernard";
        String lastName = "Comolet";
        //when
        Employee savedEmployee = employeeRepository.findByNativeSQLIndexParams(firstName, lastName);
        //then
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("JUnit Test for custom query using NativeSQL with named parameters")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject() {
        //given
        employeeRepository.save(employee1);
        String firstName = "Bernard";
        String lastName = "Comolet";
        //when
        Employee savedEmployee = employeeRepository.findByNativeSQLNamedParams(firstName, lastName);
        //then
        assertThat(savedEmployee).isNotNull();
    }

}
