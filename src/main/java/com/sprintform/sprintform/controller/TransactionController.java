package com.sprintform.sprintform.controller;

import com.sprintform.sprintform.dto.TransactionDTO;
import com.sprintform.sprintform.entity.Transaction;
import com.sprintform.sprintform.service.TransactionService;
import com.sprintform.sprintform.util.UUIDValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transaction")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDTO transactionRequest) {
        transactionService.save(transactionRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/getAllTransaction")
    public ResponseEntity<List<Transaction>> getAll() {
        return ResponseEntity.ok().body(transactionService.getTransactions());
    }

    @PutMapping("/updateTransaction/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable String id,
                                                         @RequestBody TransactionDTO transactionRequest) {
        Transaction transaction = null;
        if (UUIDValidator.isValidUUID(id)){
            transaction = transactionService.updateTransaction(id,transactionRequest);
        }
        return ResponseEntity.ok().body(transaction);
    }

    @DeleteMapping("/deleteTransaction/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable String id) {
        if (UUIDValidator.isValidUUID(id)){
            transactionService.deleteTransaction(id);
        }
        return ResponseEntity.ok("The transaction have been deleted");
    }

    @GetMapping("/predictNextMonthExpenses")
    public ResponseEntity<Double> predictNextMonthExpenses(@RequestBody(required = false) List<Transaction> plannedExpenses) {
        return ResponseEntity.ok(transactionService.predictNextMonthExpenses(plannedExpenses));
    }

}
