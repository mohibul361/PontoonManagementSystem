package com.biwta.pontoon.repository;

import com.biwta.pontoon.domain.Employee;
import com.biwta.pontoon.dto.EmployeeDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmailIgnoreCaseAndIsActiveTrue(String email);
    Boolean existsByPmsId(String pmsId);
    Boolean existsByEmployeeName(String employeeName);
    Boolean existsByPhoneNumber(String phoneNumber);
    Boolean existsByEmail(String email);
    Boolean existsByNid(String nid);
    Optional<Employee> findByIdAndIsActiveTrue(Long id);
    Optional<List<Employee>> findByIdInAndIsActiveTrue(List<Long> id);
    Page<Employee> findAllByIsActiveTrueOrderByIdDesc(Pageable pageable);
    List<Employee> findAllByIsActiveTrueOrderByIdDesc();

    @Query("SELECT e FROM Employee e " +
            "JOIN e.department d " +
            "JOIN e.section s " +
            "JOIN e.designation des " +
            "WHERE " +
            "(:keyword IS NULL OR LOWER(e.pmsId) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(e.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(e.employeeName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR TO_CHAR(e.joiningDate, 'YYYY-MM-DD') LIKE CONCAT('%', :keyword, '%') " +
            "OR LOWER(e.authority.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(des.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(s.sectionName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(d.departmentName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(e.username) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND e.isActive = true and e.department.isActive=true and e.designation.isActive=true " +
            "ORDER BY e.id DESC")
    Page<Employee> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "select\n" +
            "    p.id as pontoonId,\n" +
            "       p.pontoon_id as pontoonName,\n" +
            "       g.id as ghatId,\n" +
            "       g.ghat_name as ghatName,\n" +
            "       ps.id as sectionId,\n" +
            "       ps.section_name as sectionName,\n" +
            "       e.id as employeeId,\n" +
            "       e.employee_name as employeeName,\n" +
            "       pt.latitude as latitude,\n" +
            "       pt.longitude as longitude,\n" +
            "       pt.transfer_date as transferDate,\n" +
            "       pt.till_date as tillDate\n" +
            "from pontoon p\n" +
            "join pontoon_transfer pt on pt.pontoon_id = p.id\n" +
            "    join ghat g on pt.ghat_id = g.id\n" +
            "    join pontoon_section ps on ps.id = g.section_id\n" +
            "join pontoon_transfer_employee pte on pte.pontoon_transfer_id = pt.id\n" +
            "join employee e on e.id = pte.employee_id\n" +
            "where e.id =:pontoonId ",
            countQuery ="select count(*)from(select\n" +
                    "    p.id as pontoonId,\n" +
                    "       p.pontoon_id as pontoonName,\n" +
                    "       g.id as ghatId,\n" +
                    "       g.ghat_name as ghatName,\n" +
                    "       ps.id as sectionId,\n" +
                    "       ps.section_name as sectionName,\n" +
                    "       e.id as employeeId,\n" +
                    "       e.employee_name as employeeName,\n" +
                    "       pt.latitude as latitude,\n" +
                    "       pt.longitude as longitude,\n" +
                    "       pt.transfer_date as transferDate,\n" +
                    "       pt.till_date as tillDate\n" +
                    "from pontoon p\n" +
                    "join pontoon_transfer pt on pt.pontoon_id = p.id\n" +
                    "    join ghat g on pt.ghat_id = g.id\n" +
                    "    join pontoon_section ps on ps.id = g.section_id\n" +
                    "join pontoon_transfer_employee pte on pte.pontoon_transfer_id = pt.id\n" +
                    "join employee e on e.id = pte.employee_id\n" +
                    "where e.id =:pontoonId)",nativeQuery = true)
    Page <List<EmployeeDetails>> findAllByPontoonId(@Param("pontoonId")long pontoonId, Pageable pageable);

    Optional<Employee> findByUsernameIgnoreCaseAndIsActiveTrue(String username);

    @Query("SELECT e FROM Employee e where e.username=:username or e.email=:username and e.isActive=true")
    Optional<Employee> findByUsernameIgnoreCaseOrEmailAndIsActiveTrue(String username);
}
