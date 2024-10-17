package com.biwta.pontoon.service;

import com.biwta.pontoon.domain.Employee;
import com.biwta.pontoon.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */

public interface LoshkarService {
    Boolean addEmployee(AddEmployeeDTO addEmployeeDTO,MultipartFile employeeImage,MultipartFile employeeSignature, HttpServletRequest request);
    Employee getEmployee(Long id);
    List<Employee> getEmployees(List<Long> id);
    Page<EmployeeList> findAllEmployee(int pageNo, int pageSize, String searchKeyword);
    Page<EmployeeList> loshkarListWithPagination(int pageNo, int pageSize);
    List<EmployeeList> loshkarListWithoutPagination();
    Page<EmployeeList> loshkarListWithAdmin(int pageNo, int pageSize, String searchKeyword);
    Boolean updateEmployee(UpdateEmployeeDTO addEmployeeDTO,MultipartFile employeeImage, MultipartFile employeeSignature, HttpServletRequest request);
    Boolean deleteEmployee(Long loskharId, HttpServletRequest request);

    Page<List<EmployeeDetails>> getAllEmployeeDetails(int pageNo, int pageSize,long employeeId);

    Employee getEmployeeByUsername(String username);
    ProfileDetailsModel getProfileDetails(String username);

}
