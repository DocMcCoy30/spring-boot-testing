package com.dmcdev.springboot.controller;

import com.dmcdev.springboot.model.Employee;
import com.dmcdev.springboot.service.contract.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService employeeService;
    @Autowired
    private ObjectMapper objectMapper;
    private Employee employee1;

    @BeforeEach
    void setUp() {
        employee1 = Employee.builder()
                .firstName("Bernard")
                .lastName("Comolet")
                .email("comolet@mail.com")
                .build();
    }

    @DisplayName("JUnit Test for create employee REST API")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
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

    @DisplayName("JUnit Test for get all employees REST API")
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
        //given
        List<Employee> employeeList = new ArrayList<>();
        Employee employee2 = Employee.builder()
                .firstName("Bruno")
                .lastName("Francard")
                .email("francard@mail.com")
                .build();
        employeeList.add(employee1);
        employeeList.add(employee2);
        given(employeeService.getAllEmployees()).willReturn(employeeList);
        //when
        ResultActions response = mockMvc.perform(get("/api/employees"));
        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(employeeList.size())));

    }

    @DisplayName("JUnit Test for get employee by id REST API - Positive scenarion : valid id")
    @Test
    public void givenValidEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //given
        long employeeId = 1L;
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee1));
        //when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));
        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee1.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee1.getLastName())))
                .andExpect(jsonPath("$.email", is(employee1.getEmail())));
    }

    @DisplayName("JUnit Test for get employee by id REST API - Negative scenarion : unvalid id")
    @Test
    public void givenUnvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        //given
        long employeeId = 2L;
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        //when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));
        //then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("JUnit Test for update employee REST API - Positive scenario : valid id")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
        //given
        long employeeId = 1L;
        Employee updatedEmployee = Employee.builder()
                .firstName("updatedFirstName")
                .lastName("updatedLastName")
                .email("updatedEmail@mail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee1));
        given(employeeService.updateEmployee(any(Employee.class))).willAnswer((invocation) -> invocation.getArgument(0));
        //when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));
        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is("updatedFirstName")))
                .andExpect(jsonPath("$.lastName", is("updatedLastName")))
                .andExpect(jsonPath("$.email", is("updatedEmail@mail.com")));
    }

    @DisplayName("JUnit Test for update employee REST API - Negative scenario : unvalid id")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {
        //given
        long employeeId = 1L;
        Employee updatedEmployee = Employee.builder()
                .firstName("updatedFirstName")
                .lastName("updatedLastName")
                .email("updatedEmail@mail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class))).willAnswer((invocation) -> invocation.getArgument(0));
        //when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));
        //then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("JUnit Test for delete employee REST API")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
        //given
        long employeeId = 1L;
        willDoNothing().given(employeeService).deleteEmploye(employeeId);
        //when
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));
        //then
        response.andExpect(status().isOk())
                .andDo(print());
    }


}