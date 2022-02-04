package com.tms.service;

import java.util.List;

import com.tms.api.model.EmployeeInfo;
import com.tms.dto.Employee;

import org.springframework.stereotype.Service;

@Service
public interface EmployeeService {

    EmployeeInfo getEmployee(String payrollId);

    EmployeeInfo getEmployee(Integer employeeId);

    List<Integer> getEmployeeCampuses(Integer employeeId);

    List<Employee> getCampusEmployees(List<Integer> campuses);
}
