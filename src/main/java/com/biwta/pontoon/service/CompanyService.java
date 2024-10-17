package com.biwta.pontoon.service;

import com.biwta.pontoon.domain.Company;
import com.biwta.pontoon.dto.AddCompany;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author nasimkabir
 * ৩/১২/২৩
 */
public interface CompanyService {
    Company addCompany(AddCompany addCompany, HttpServletRequest request);
    Company getCompany(long companyId);
    List<Company> companyList();
    Company updateCompany(Long companyId, AddCompany updateCompany, HttpServletRequest request);
    void deleteCompany(Long companyId, HttpServletRequest request);
}
