package com.biwta.pontoon.repository;

import com.biwta.pontoon.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author nasimkabir
 * ৩/১২/২৩
 */
public interface CompanyRepository extends JpaRepository<Company,Long> {
    Boolean existsByCompanyName(String companyName);
}
