package com.dmcdev.springboot.integration;

import com.dmcdev.springboot.model.Employee;
import com.dmcdev.springboot.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITLocal {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
    }

    @DisplayName("JUnit Integration Test for create employee REST API")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given
        Employee employee1 = Employee.builder()
                .firstName("Bernard")
                .lastName("Comolet")
                .email("comolet@mail.com")
                .build();
        //when
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee1)));
        //then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(employee1.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(employee1.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(employee1.getEmail())));
    }

    @DisplayName("JUnit Integration Test for get all employees REST API")
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
        //given
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(Employee.builder()
                .firstName("Bernard")
                .lastName("Comolet")
                .email("comolet@mail.com")
                .build());
        employeeList.add(Employee.builder()
                .firstName("Bruno")
                .lastName("Francard")
                .email("francard@mail.com")
                .build());
        employeeRepository.saveAll(employeeList);
        //when
        ResultActions response = mockMvc.perform(get("/api/employees"));
        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(employeeList.size())));

    }

    @DisplayName("JUnit Integration Test for get employee by id REST API - Positive scenario : valid id")
    @Test
    public void givenValidEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //given
        Employee employee1 = Employee.builder()
                .firstName("Bernard")
                .lastName("Comolet")
                .email("comolet@mail.com")
                .build();
        employeeRepository.save(employee1);
        //when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee1.getId()));
        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee1.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee1.getLastName())))
                .andExpect(jsonPath("$.email", is(employee1.getEmail())));
    }

    @DisplayName("JUnit Integration Test for get employee by id REST API - Negative scenario : unvalid id")
    @Test
    public void givenUnvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        //given
        long employeeId = 2L;
        Employee employee1 = Employee.builder()
                .firstName("Bernard")
                .lastName("Comolet")
                .email("comolet@mail.com")
                .build();
        employeeRepository.save(employee1);
        //when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));
        //then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("JUnit Integration Test for update employee REST API - Positive scenario : valid id")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
        //given
        Employee savedEmployee = Employee.builder()
                .firstName("Bernard")
                .lastName("Comolet")
                .email("comolet@mail.com")
                .build();
        employeeRepository.save(savedEmployee);
        Employee updatedEmployee = Employee.builder()
                .firstName("updatedFirstName")
                .lastName("updatedLastName")
                .email("updatedEmail@mail.com")
                .build();
        //when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));
        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is("updatedFirstName")))
                .andExpect(jsonPath("$.lastName", is("updatedLastName")))
                .andExpect(jsonPath("$.email", is("updatedEmail@mail.com")));
    }

    @DisplayName("JUnit Integration Test for update employee REST API - Negative scenario : unvalid id")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {
        //given
        Employee savedEmployee = Employee.builder()
                .firstName("Bernard")
                .lastName("Comolet")
                .email("comolet@mail.com")
                .build();
        employeeRepository.save(savedEmployee);
        Employee updatedEmployee = Employee.builder()
                .firstName("updatedFirstName")
                .lastName("updatedLastName")
                .email("updatedEmail@mail.com")
                .build();
        //when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId()-1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));
        //then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("JUnit Integration Test for delete employee REST API")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
        //given
        Employee savedEmployee = Employee.builder()
                .firstName("Bernard")
                .lastName("Comolet")
                .email("comolet@mail.com")
                .build();
        employeeRepository.save(savedEmployee);
        //when
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", savedEmployee.getId()));
        //then
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
