package com.biwta.pontoon.service.impl;

import com.biwta.pontoon.domain.*;
import com.biwta.pontoon.dto.*;
import com.biwta.pontoon.repository.AuthorityRepository;
import com.biwta.pontoon.repository.EmployeeRepository;
import com.biwta.pontoon.repository.UserRepository;
import com.biwta.pontoon.service.*;
import com.biwta.pontoon.utils.FileExtensionUtil;
import com.biwta.pontoon.utils.FileUploadUtil;
import com.biwta.pontoon.utils.PhoneNumberValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LoshkarServiceImpl implements LoshkarService {
    private final AuthorityRepository authorityRepository;
    private final EmployeeRepository employeeRepository;
    private final PontoonDivisionService pontoonDivisionService;
    private final PontoonDepartmentService pontoonDepartmentService;
    private final PontoonSectionService pontoonSectionService;
    private final TransactionService transactionService;
    private final Environment ENVIRONMENT;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userService;
    private final DesignationService designationService;

    @Transactional(rollbackFor = Exception.class)
    public Boolean addEmployee(AddEmployeeDTO addEmployeeDTO, MultipartFile employeeImage, MultipartFile employeeSignature, HttpServletRequest request) {
        String uploadDirEmployee = ENVIRONMENT.getProperty("fileStore.directory") + "/employeeImage/";
        String uploadDirEmployeeDoc = ENVIRONMENT.getProperty("fileStore.directory") + "/employeeSignatureImage/";
        String uploadDirUserImage = ENVIRONMENT.getProperty("fileStore.directory") + "/userImage/";

        String randomNumber = RandomStringUtils.randomNumeric(6);


        if (employeeRepository.existsByPmsId(addEmployeeDTO.getPmsId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee already exists with pmsId:" + addEmployeeDTO.getPmsId());
        }
        if (employeeRepository.existsByEmail(addEmployeeDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee already exists with email:" + addEmployeeDTO.getEmail());
        }
        if (employeeRepository.existsByPhoneNumber(addEmployeeDTO.getPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee already exists with phoneNumber:" + addEmployeeDTO.getPhoneNumber());
        }
        if (employeeRepository.existsByNid(addEmployeeDTO.getNid())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee already exists with nid:" + addEmployeeDTO.getNid());
        }


        Authority authority = authorityRepository.findByName(addEmployeeDTO.getAuthorityId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Authority not found with id:" + addEmployeeDTO.getAuthorityId()));
        PontoonDivision pontoonDivision = pontoonDivisionService.findPontoonDivisionById(addEmployeeDTO.getDivisionId());
        PontoonSection pontoonSection = pontoonSectionService.findPontoonDivisionById(addEmployeeDTO.getSectionId());
        Employee employee = new Employee();
        employee.setPmsId(addEmployeeDTO.getPmsId());
        employee.setEmployeeName(addEmployeeDTO.getEmployeeName());
        employee.setEmail(addEmployeeDTO.getEmail());
        employee.setPassword(passwordEncoder.encode(addEmployeeDTO.getPassword()));
        employee.setPhoneNumber(addEmployeeDTO.getPhoneNumber());
        if (PhoneNumberValidator.isValidBangladeshiPhoneNumber(addEmployeeDTO.getPhoneNumber())) {
            employee.setPhoneNumber(addEmployeeDTO.getPhoneNumber());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid phone number");
        }
        employee.setNid(addEmployeeDTO.getNid());
        employee.setUsername(addEmployeeDTO.getUsername());
        employee.setDivision(pontoonDivision);
        if (addEmployeeDTO.getDepartmentId() == null) {
            employee.setDepartment(null);
        } else {
            PontoonDepartment pontoonDepartment = pontoonDepartmentService.findPontoonDivisionById(addEmployeeDTO.getDepartmentId());
            employee.setDepartment(pontoonDepartment);
        }
//        employee.setDepartment(pontoonDepartment);
        if (addEmployeeDTO.getSectionId() == null) {
            employee.setSection(null);
        } else {
            PontoonSection pontoonSection1 = pontoonSectionService.findPontoonDivisionById(addEmployeeDTO.getSectionId());
            employee.setSection(pontoonSection1);
        }
//        employee.setSection(pontoonSection);
        employee.setAuthority(authority);
        employee.setIsActive(true);
        employee.setEmployeeRemarks(addEmployeeDTO.getEmployeeRemarks());
        employee.setDesignation(designationService.getDesignation(addEmployeeDTO.getDesignationId()));
        employee.setJoiningDate(addEmployeeDTO.getJoiningDate());

        if (employeeImage == null) {
//           String defaultImage = "https://www.nicepng.com/maxp/u2y3a9e6t4o0a9w7";
            employee.setEmployeeImage(null);
           /* try {
                FileUploadUtil.saveFile(uploadDirEmployee, randomNumber + "_img_" + defaultImage.trim(), employeeImage);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        } else {
            String employeeImageName = StringUtils.cleanPath(employeeImage.getOriginalFilename());
// Check if the file extension is allowed for employeeImage
            if (!FileExtensionUtil.isImageExtension(employeeImage.getOriginalFilename())) {
                throw new IllegalArgumentException("Invalid file type for employeeImage. Allowed types are jpg, jpeg, png.");
            }
            employee.setEmployeeImage(randomNumber + "_img_" + employeeImageName.trim());
            try {
                FileUploadUtil.saveFile(uploadDirEmployee, randomNumber + "_img_" + employeeImageName.trim(), employeeImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (employeeSignature == null) {
            employee.setEmployeeSignature(null);
        } else {
            String employeeSignatureName = StringUtils.cleanPath(employeeSignature.getOriginalFilename());
            // Check if the file extension is allowed for employeeSignature
            if (!FileExtensionUtil.isImageExtension(employeeSignature.getOriginalFilename())) {
                throw new IllegalArgumentException("Invalid file type for employeeSignature. Allowed types are jpg, jpeg, png.");
            }
            employee.setEmployeeSignature(randomNumber + "_img_" + employeeSignatureName.trim());
            try {
                FileUploadUtil.saveFile(uploadDirEmployeeDoc, randomNumber + "_img_" + employeeSignatureName.trim(), employeeSignature);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Employee e = employeeRepository.save(employee);
        Transaction t = transactionService.addTransaction(new TransactionDTO("Add Employee."), request);

        // also update user
        Users user = new Users();
        user.setFirstName(addEmployeeDTO.getEmployeeName());
        user.setLastName(addEmployeeDTO.getEmployeeName());
        user.setEmail(addEmployeeDTO.getEmail());
        user.setUsername(addEmployeeDTO.getUsername());
        user.setPassword(passwordEncoder.encode(addEmployeeDTO.getPassword()));
        user.setActivated(true);
        user.setEmployee(e);
        user.setAuthorities(Collections.singleton(authority));
        if (employeeImage == null) {
            employee.setEmployeeImage(null);
        } else {
            String employeeImageName = StringUtils.cleanPath(employeeImage.getOriginalFilename());
            if (!FileExtensionUtil.isImageExtension(employeeImage.getOriginalFilename())) {
                throw new IllegalArgumentException("Invalid file type for employeeImage. Allowed types are jpg, jpeg, png.");
            }
            user.setImageUrl(randomNumber + "_img_" + employeeImageName.trim());
            try {
                FileUploadUtil.saveFile(uploadDirUserImage, randomNumber + "_img_" + employeeImageName.trim(), employeeImage);
            } catch (IOException ee) {
                ee.printStackTrace();
            }
        }
        userService.save(user);

        if (e != null && t != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Employee getEmployee(Long id) {
        return employeeRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id:" + id));
    }

    @Override
    public List<Employee> getEmployees(List<Long> ids) {
        return employeeRepository.findByIdInAndIsActiveTrue(ids)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id:" + ids));
    }

    @Override
    public Page<EmployeeList> findAllEmployee(int pageNo, int pageSize, String searchKeyword) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Streamable<EmployeeList> employeeStream;

        if (searchKeyword == null || searchKeyword.isEmpty()) {
            employeeStream = employeeRepository.findAllByIsActiveTrueOrderByIdDesc(pageable)
                    .stream()
                    .filter(employee ->
                            designationService.getDesignation(employee.getDesignation().getId()).getName().equals("Loshkor"))
                    .map(this::mapEmployeeToEmployeeList)
                    .collect(Collectors.collectingAndThen(Collectors.toList(), Streamable::of));
        } else {
            employeeStream = employeeRepository.findAllByKeyword(searchKeyword, pageable)
                    .stream()
                    .filter(employee ->
                            designationService.getDesignation(employee.getDesignation().getId()).getName().equals("Loshkor"))
                    .map(this::mapEmployeeToEmployeeList)
                    .collect(Collectors.collectingAndThen(Collectors.toList(), Streamable::of));
        }

        List<EmployeeList> employeeList = employeeStream.toList();

        if (employeeList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No data found");
        }

        return new PageImpl<>(employeeList, pageable, employeeList.size());
    }


    @Override
    public Page<EmployeeList> loshkarListWithPagination(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        // filter by department
        Page<EmployeeList> employeeList = employeeRepository.findAllByIsActiveTrueOrderByIdDesc(pageable)
                .map(employee -> mapEmployeeToEmployeeList(employee));
        if (employeeList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No data found");
        }
        return employeeList;
    }

    @Override
    public List<EmployeeList> loshkarListWithoutPagination() {
        List<Employee> employeeList = employeeRepository.findAllByIsActiveTrueOrderByIdDesc()
                .stream()
                .filter(employee ->
                        designationService.getDesignation(employee.getDesignation().getId()).getName().equals("Loshkor"))
                .collect(Collectors.toList());
        if (employeeList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No data found");
        }
        return employeeList.stream().map(this::mapEmployeeToEmployeeList).collect(Collectors.toList());
    }

    @Override
    public Page<EmployeeList> loshkarListWithAdmin(int pageNo, int pageSize, String searchKeyword) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<EmployeeList> employeePage;

        if (searchKeyword == null || searchKeyword.isEmpty()) {
            employeePage = employeeRepository.findAllByIsActiveTrueOrderByIdDesc(pageable)
                    .map(this::mapEmployeeToEmployeeList);
        } else {
            employeePage = employeeRepository.findAllByKeyword(searchKeyword, pageable)
                    .map(this::mapEmployeeToEmployeeList);
        }

        if (!employeePage.hasContent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No data found");
        }

        return employeePage;
    }


    @Override
    public Boolean updateEmployee(UpdateEmployeeDTO addEmployeeDTO, MultipartFile employeeImage, MultipartFile employeeSignature, HttpServletRequest request) {
        return employeeRepository.findByIdAndIsActiveTrue(addEmployeeDTO.getLoskharId())
                .map(employee -> {
                    if (addEmployeeDTO.getEmployeeName() != null) {
                        employee.setEmployeeName(addEmployeeDTO.getEmployeeName());
                    }
                    if (addEmployeeDTO.getUsername() != null) {
                        employee.setUsername(addEmployeeDTO.getUsername());
                        userService.findByEmailOrUsernameIgnoreCaseAndActivated(employee.getUsername(), true)
                                .ifPresent(user -> {
                                    user.setUsername(addEmployeeDTO.getUsername());
                                    userService.save(user);
                                });
                    }
                    if (addEmployeeDTO.getPhoneNumber() != null) {
                        employee.setPhoneNumber(addEmployeeDTO.getPhoneNumber());
                    }
                    if (addEmployeeDTO.getEmail() != null) {
                        employee.setEmail(addEmployeeDTO.getEmail());
                        userService.findByEmailOrUsernameIgnoreCaseAndActivated(employee.getUsername(), true)
                                .ifPresent(user -> {
                                    user.setEmail(addEmployeeDTO.getEmail());
                                    userService.save(user);
                                });
                    }
                    if (addEmployeeDTO.getNid() != null) {
                        employee.setNid(addEmployeeDTO.getNid());
                    }
                    if (addEmployeeDTO.getEmployeeRemarks() != null) {
                        employee.setEmployeeRemarks(addEmployeeDTO.getEmployeeRemarks());
                    }
                    if (addEmployeeDTO.getDesignationId() != 0) {
                        employee.setDesignation(designationService.getDesignation(addEmployeeDTO.getDesignationId()));
                    }
                    if (addEmployeeDTO.getJoiningDate() != null) {
                        employee.setJoiningDate(addEmployeeDTO.getJoiningDate());
                    }
                    if (addEmployeeDTO.getDivisionId() != null) {
                        employee.setDivision(pontoonDivisionService.findPontoonDivisionById(addEmployeeDTO.getDivisionId()));
                    }
                    if (addEmployeeDTO.getDepartmentId() != null) {
                        employee.setDepartment(pontoonDepartmentService.findPontoonDivisionById(addEmployeeDTO.getDepartmentId()));
                    }
                    if (addEmployeeDTO.getSectionId() != null) {
                        employee.setSection(pontoonSectionService.findPontoonDivisionById(addEmployeeDTO.getSectionId()));
                    }
                    if (employeeImage != null) {
                        String uploadDirEmployee = ENVIRONMENT.getProperty("fileStore.directory") + "/employeeImage/";
                        String uploadDirUserImage = ENVIRONMENT.getProperty("fileStore.directory") + "/userImage/";

                        String randomNumber = RandomStringUtils.randomNumeric(6);
                        String employeeImageName = StringUtils.cleanPath(employeeImage.getOriginalFilename());
// Check if the file extension is allowed for employeeImage
                        if (!FileExtensionUtil.isImageExtension(employeeImage.getOriginalFilename())) {
                            throw new IllegalArgumentException("Invalid file type for employeeImage. Allowed types are jpg, jpeg, png.");
                        }
                        employee.setEmployeeImage(randomNumber + "_img_" + employeeImageName.trim());
                        userService.findByEmailOrUsernameIgnoreCaseAndActivated(employee.getUsername(), true)
                                .ifPresent(user -> {
                                    user.setImageUrl(randomNumber + "_img_" + employeeImageName.trim());
                                    userService.save(user);
                                });
                        try {
                            FileUploadUtil.saveFile(uploadDirEmployee, randomNumber + "_img_" + employeeImageName.trim(), employeeImage);
                            FileUploadUtil.saveFile(uploadDirUserImage, randomNumber + "_img_" + employeeImageName.trim(), employeeImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (employeeSignature != null) {
                        String uploadDirEmployeeDoc = ENVIRONMENT.getProperty("fileStore.directory") + "/employeeSignatureImage/";

                        String randomNumber = RandomStringUtils.randomNumeric(6);
                        String employeeSignatureName = StringUtils.cleanPath(employeeSignature.getOriginalFilename());
                        // Check if the file extension is allowed for employeeSignature
                        if (!FileExtensionUtil.isImageExtension(employeeSignature.getOriginalFilename())) {
                            throw new IllegalArgumentException("Invalid file type for employeeSignature. Allowed types are jpg, jpeg, png.");
                        }
                        employee.setEmployeeSignature(randomNumber + "_img_" + employeeSignatureName.trim());
                        try {
                            FileUploadUtil.saveFile(uploadDirEmployeeDoc, randomNumber + "_img_" + employeeSignatureName.trim(), employeeSignature);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (addEmployeeDTO.getPassword() != null) {
                        employee.setPassword(passwordEncoder.encode(addEmployeeDTO.getPassword()));
                        userService.findByEmailOrUsernameIgnoreCaseAndActivated(employee.getUsername(), true)
                                .ifPresent(user -> {
                                    user.setPassword(passwordEncoder.encode(addEmployeeDTO.getPassword()));
                                    userService.save(user);
                                });
                    } else {
                        employee.setPassword(employee.getPassword());
                        userService.findByEmailOrUsernameIgnoreCaseAndActivated(employee.getUsername(), true)
                                .ifPresent(user -> {
                                    user.setPassword(passwordEncoder.encode(employee.getPassword()));
                                    userService.save(user);
                                });
                    }
                    Employee p = employeeRepository.save(employee);
                    Transaction t = transactionService.addTransaction(new TransactionDTO("Update Employee."), request);

                    if (p != null && t != null) {
                        return true;
                    } else {
                        return false;
                    }
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pontoon not found with id: " + addEmployeeDTO.getLoskharId() + ""));
    }

    @Override
    public Boolean deleteEmployee(Long loskharId, HttpServletRequest request) {
        return employeeRepository.findByIdAndIsActiveTrue(loskharId)
                .map(employee -> {
                    employee.setIsActive(false);
                    employeeRepository.save(employee);
                    transactionService.addTransaction(new TransactionDTO("Delete Employee."), request);
                    return true;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id:" + loskharId));
    }

    @Override
    public Page<List<EmployeeDetails>> getAllEmployeeDetails(int pageNo, int pageSize, long employeeId) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<List<EmployeeDetails>> lists = employeeRepository.findAllByPontoonId(employeeId, pageable);
        if (lists.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data found");
        }
        return lists;
    }

    @Override
    public Employee getEmployeeByUsername(String username) {
        return this.employeeRepository.findByUsernameIgnoreCaseAndIsActiveTrue(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with username:" + username));
    }

    @Override
    public ProfileDetailsModel getProfileDetails(String username) {
        return this.employeeRepository.findByUsernameIgnoreCaseOrEmailAndIsActiveTrue(username)
                .map(employee -> {
                    ProfileDetailsModel profileDetailsModel = new ProfileDetailsModel();
                    profileDetailsModel.setEmployeeName(employee.getEmployeeName());
                    profileDetailsModel.setEmployeeImage(employee.getEmployeeImage());
                    profileDetailsModel.setDesignation(employee.getDesignation().getName());
                    profileDetailsModel.setAuthority(employee.getAuthority().getName());
                    profileDetailsModel.setJoiningDate(String.valueOf(employee.getJoiningDate()));
                    profileDetailsModel.setPhoneNumber(employee.getPhoneNumber());
                    profileDetailsModel.setEmail(employee.getEmail());
                    profileDetailsModel.setNid(employee.getNid());
                    profileDetailsModel.setPmsId(employee.getPmsId());
                    return profileDetailsModel;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with username:" + username));
    }

    private EmployeeList mapEmployeeToEmployeeList(Employee employeeList) {
        EmployeeList emp = new EmployeeList();
        emp.setId(employeeList.getId());
        emp.setPmsId(employeeList.getPmsId());
        emp.setEmployeeName(employeeList.getEmployeeName());
        emp.setDesignation(employeeList.getDesignation().getName());
        emp.setPhoneNumber(employeeList.getPhoneNumber());
        emp.setEmail(employeeList.getEmail());
        emp.setNid(employeeList.getNid());
        emp.setStatus(employeeList.getIsActive());
        emp.setAuthority(employeeList.getAuthority().getName());
        emp.setDepartment(employeeList.getDepartment().getDepartmentName());
        emp.setSection(employeeList.getSection().getSectionName());
        emp.setJoiningDate(String.valueOf(employeeList.getJoiningDate()));
        emp.setEmployeeImage(employeeList.getEmployeeImage() == null ? "img.png" : employeeList.getEmployeeImage());
        return emp;
    }
}
