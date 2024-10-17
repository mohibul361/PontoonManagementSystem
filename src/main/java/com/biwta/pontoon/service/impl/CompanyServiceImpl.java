package com.biwta.pontoon.service.impl;

import com.biwta.pontoon.domain.Company;
import com.biwta.pontoon.dto.AddCompany;
import com.biwta.pontoon.repository.CompanyRepository;
import com.biwta.pontoon.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author nasimkabir
 * ৩/১২/২৩
 */
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;

    @Override
    public Company addCompany(AddCompany addCompany, HttpServletRequest request) {
        Company company = new Company();
        if (companyRepository.existsByCompanyName(addCompany.getCompanyName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company name already exits");
        }
        company.setCompanyName(addCompany.getCompanyName());
        company.setBin(addCompany.getBin());
        company.setAddress(addCompany.getAddress());
        company.setIsActive(true);
        return companyRepository.save(company);
    }

    @Override
    public Company getCompany(long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company id not found with id is " + companyId));
    }

    @Override
    public List<Company> companyList() {
        return companyRepository.findAll();
    }

    @Override
    public Company updateCompany(Long companyId, AddCompany updateCompany, HttpServletRequest request) {
        return companyRepository.findById(companyId)
                .map(existingCompany -> {
                    if (companyRepository.existsByCompanyName(updateCompany.getCompanyName())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company name already exits");
                    }
                    if (updateCompany.getCompanyName() != null) {
                        existingCompany.setCompanyName(updateCompany.getCompanyName());
                    }
                    if (updateCompany.getBin() != null) {
                        existingCompany.setBin(updateCompany.getBin());
                    }
                    if (updateCompany.getAddress() != null) {
                        existingCompany.setAddress(updateCompany.getAddress());
                    }
                    return companyRepository.save(existingCompany);
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company id not found with id is " + companyId));
    }

    @Override
    public void deleteCompany(Long companyId, HttpServletRequest request) {
        companyRepository.findById(companyId)
                .map(existingCompany -> {
                    existingCompany.setIsActive(false);
                    return companyRepository.save(existingCompany);
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company id not found with id is " + companyId));
    }

}
