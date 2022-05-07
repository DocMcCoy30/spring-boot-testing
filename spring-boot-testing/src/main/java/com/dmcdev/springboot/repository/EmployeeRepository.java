package com.dmcdev.springboot.repository;

import com.dmcdev.springboot.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    //define custom query using JPQL with index parameters
    @Query("select e from Employee e where e.firstName = ?1 and e.lastName = ?2")
    Employee findByJPQLIndexParams(String firstName, String lastName);

    //define custom query using JPQL with named parameters
    @Query("select e from Employee e where e.firstName =:firstName and e.lastName =:lastName")
    Employee findByJPQLNamedParams(@Param("firstName") String firstName, @Param("lastName") String lastName);

    //define custom query using Native SQL with index parameters
    @Query(value = "select * from employees e where e.first_name = ?1 and e.last_name = ?2", nativeQuery = true)
    Employee findByNativeSQLIndexParams(String firstName, String lastName);

    //define custom query using Native SQL with named parameters
    @Query(value = "select * from employees e where e.first_name =:firstName and e.last_name =:lastName", nativeQuery = true)
    Employee findByNativeSQLNamedParams(@Param("firstName") String firstName, @Param("lastName") String lastName);

}
