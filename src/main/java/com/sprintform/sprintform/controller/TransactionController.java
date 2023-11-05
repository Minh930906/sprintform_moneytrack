package com.sprintform.sprintform.controller;

import com.sprintform.sprintform.dto.Category;
import com.sprintform.sprintform.dto.NextMonthExpenseDTO;
import com.sprintform.sprintform.dto.RequestTransactionDTO;
import com.sprintform.sprintform.dto.TransactionDTO;
import com.sprintform.sprintform.entity.Transaction;
import com.sprintform.sprintform.service.TransactionService;
import com.sprintform.sprintform.util.UUIDValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transaction")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody RequestTransactionDTO requestTransactionDTO) {
        transactionService.save(requestTransactionDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getAllTransaction")
    public ResponseEntity<List<Transaction>> getAll(@RequestParam(name = "category", required = false) Category category,
                                                    @RequestParam(name = "startDate", required = false) LocalDate startDate,
                                                    @RequestParam(name = "endDate", required = false) LocalDate endDate) {
        return ResponseEntity.ok().body(transactionService.getTransactions(category,startDate,endDate));
    }

    @PutMapping("/updateTransaction/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable String id,
                                                         @RequestBody TransactionDTO transactionRequest) {
        Transaction transaction = null;
        if (UUIDValidator.isValidUUID(id)) {
            transaction = transactionService.updateTransaction(id, transactionRequest);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteTransaction/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable String id) {
        if (UUIDValidator.isValidUUID(id)) {
            transactionService.deleteTransaction(id);
        }
        return ResponseEntity.ok("The transaction have been deleted");
    }

    @GetMapping("/predictNextMonthExpenses")
    public ResponseEntity<NextMonthExpenseDTO> predictNextMonthExpenses() {
        return ResponseEntity.ok(transactionService.predictNextMonthExpenses());
    }

}
