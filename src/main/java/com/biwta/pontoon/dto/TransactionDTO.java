package com.biwta.pontoon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private String description;
}
