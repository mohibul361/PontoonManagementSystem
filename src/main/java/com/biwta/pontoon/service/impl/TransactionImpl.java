package com.biwta.pontoon.service.impl;

import com.biwta.pontoon.domain.Transaction;
import com.biwta.pontoon.repository.TransactionRepository;
import com.biwta.pontoon.service.TransactionService;
import com.biwta.pontoon.dto.TransactionDTO;
import com.biwta.pontoon.utils.EntityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    @Override
    public Transaction addTransaction(TransactionDTO transactionDTO, HttpServletRequest request) {
        Transaction transaction = new Transaction();
        transaction.setDescription(transactionDTO.getDescription());
        EntityUtils.setAdd(transaction, request);
        return transactionRepository.save(transaction);
    }
}
