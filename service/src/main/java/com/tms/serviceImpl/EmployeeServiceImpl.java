package com.tms.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tms.api.model.EmployeeInfo;
import com.tms.configuration.JwtUtils;
import com.tms.dto.Employee;
import com.tms.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public EmployeeInfo getEmployee(String payrollId) {
        try {
           
            jwtUtils.headers.set("Authorization", jwtUtils.getToken());
            jwtUtils.entity = new HttpEntity(jwtUtils.headers);
            EmployeeInfo employee = jwtUtils.restTemplateUtil.getForEntity(EmployeeInfo.class,
            jwtUtils.hrmsBaseUrl + "/api/v1/employee?payrollId={payrollId}", jwtUtils.entity, payrollId);
            return employee;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public EmployeeInfo getEmployee(Integer employeeId) {
        try {
            
            jwtUtils.headers.set("Authorization", jwtUtils.getToken());
            jwtUtils.entity = new HttpEntity(jwtUtils.headers);
            EmployeeInfo employee = jwtUtils.restTemplateUtil.getForEntity(EmployeeInfo.class,
            jwtUtils.hrmsBaseUrl + "/api/v1/employee?employeeId={employeeId}", jwtUtils.entity, employeeId);
            return employee;
        } catch (Exception e) {
            return null;
        }
    }

   

    @Override
    public List<Employee> getCampusEmployees(List<Integer> campuses) {
        try {
            jwtUtils.headers.set("Authorization", jwtUtils.getToken());
            jwtUtils.entity = new HttpEntity(jwtUtils.headers);
            Employee[] campusEmployees = jwtUtils.restTemplateUtil.postForEntity(Employee[].class, jwtUtils.hrmsBaseUrl + "/api/v1/employees",
            jwtUtils.entity, campuses);
            if (campusEmployees != null && campusEmployees.length > 0) {
                return new ArrayList<>(Arrays.asList(campusEmployees));
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Integer> getEmployeeCampuses(Integer employeeId) {
        // TODO Auto-generated method stub
        return null;
    }
}
