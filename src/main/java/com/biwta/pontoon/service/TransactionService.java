package com.biwta.pontoon.service;

import com.biwta.pontoon.domain.Transaction;
import com.biwta.pontoon.dto.TransactionDTO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
public interface TransactionService {
    Transaction addTransaction(TransactionDTO transactionDTO, HttpServletRequest request);
}
