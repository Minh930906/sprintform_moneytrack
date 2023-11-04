package com.sprintform.sprintform.service;

import com.sprintform.sprintform.dto.TransactionDTO;
import com.sprintform.sprintform.entity.Transaction;
import com.sprintform.sprintform.exception.TransactionNotFoundException;
import com.sprintform.sprintform.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepository transactionRepository;

    public List<Transaction> getTransactions() {

        return transactionRepository.findAll();
    }


    public Transaction save(TransactionDTO transactionDTO) {

        logger.info("New transaction have been created");

        return transactionRepository.save(Transaction.builder()
                .amount(transactionDTO.getAmount())
                .description(transactionDTO.getDescription())
                .date(transactionDTO.getDate())
                .build());


    }

    public Transaction updateTransaction(String id, TransactionDTO transactionRequest) {

        UUID uuid = UUID.fromString(id);

        Transaction transaction = transactionRepository.findById(uuid)
                .orElseThrow(() -> new TransactionNotFoundException("The specified transaction id cannot be found"));

        if (transactionRequest.getAmount() != null) {
            transaction.setAmount(transactionRequest.getAmount());
        }
        if (transactionRequest.getDescription() != null) {
            transaction.setDescription(transactionRequest.getDescription());
        }
        if (transactionRequest.getDate() != null) {
            transaction.setDate(transactionRequest.getDate());
        }

        logger.info("Transaction " + id + " have been updated");

        return transactionRepository.save(transaction);

    }

    public void deleteTransaction(String id) {

        UUID uuid = UUID.fromString(id);

        Transaction transaction = transactionRepository.findById(uuid)
                .orElseThrow(() -> new TransactionNotFoundException("The specified transaction id cannot be found"));

        logger.info("Transaction " + id + " have been deleted");

        transactionRepository.delete(transaction);

    }

    public Double predictNextMonthExpenses(List<Transaction> plannedExpenses) {
        LocalDate currentDate = LocalDate.now();
        LocalDate lastMonth = currentDate.minusMonths(1);

        List<Transaction> transactions = transactionRepository.findAll();

        List<Transaction> lastMonthTransactions = transactions.stream()
                .filter(transaction -> transaction.getDate().getMonth() == lastMonth.getMonth())
                .collect(Collectors.toList());

        Double lastMonthExpenses = lastMonthTransactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();

        Double totalPlannedExpenses;
        if (plannedExpenses == null || plannedExpenses.isEmpty()) {
            totalPlannedExpenses = 0.0;
        } else {
            totalPlannedExpenses = plannedExpenses.stream()
                    .mapToDouble(Transaction::getAmount)
                    .sum();
        }


        return lastMonthExpenses + totalPlannedExpenses;
    }
}
