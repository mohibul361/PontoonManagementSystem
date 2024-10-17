package com.biwta.pontoon.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author nasimkabir
 * ৩/১২/২৩
 */
@Data
public class AddCompany {
    @NotNull(message = "Company Name can not null")
    private String companyName;
    private String bin;
    private String address;
}
