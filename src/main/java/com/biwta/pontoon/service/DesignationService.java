package com.biwta.pontoon.service;

import com.biwta.pontoon.domain.Designation;

import java.util.List;

/**
 * @author nasimkabir
 * ১৮/১২/২৩
 */
public interface DesignationService {
    Boolean addDesignation(String name, Long departmentId);
    Boolean updateDesignation(Long id, String name, Long departmentId);
    Boolean deleteDesignation(Long id);
    Designation getDesignation(Long id);
    List<Designation> getAllDesignation();
}
