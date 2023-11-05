package com.sprintform.sprintform.service;

import com.sprintform.sprintform.dto.Category;
import com.sprintform.sprintform.dto.NextMonthExpenseDTO;
import com.sprintform.sprintform.dto.RequestTransactionDTO;
import com.sprintform.sprintform.dto.TransactionDTO;
import com.sprintform.sprintform.entity.Transaction;
import com.sprintform.sprintform.exception.TransactionNotFoundException;
import com.sprintform.sprintform.repository.TransactionDAO;
import com.sprintform.sprintform.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionDAO transactionDAO;
    private final TransactionRepository transactionRepository;

    public List<Transaction> getTransactions(Category category, LocalDate startDate, LocalDate endDate) {
        return transactionDAO.findEntities(category, startDate, endDate);

    }


    public Transaction save(RequestTransactionDTO transactionDTO) {

        logger.info("New transaction have been created");

        return transactionRepository.save(Transaction.builder()
                .amount(transactionDTO.getAmount())
                .description(transactionDTO.getDescription())
                .category(transactionDTO.getCategory())
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
        if (transactionRequest.getCategory() != null) {
            transaction.setCategory(transactionRequest.getCategory());
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

    public NextMonthExpenseDTO predictNextMonthExpenses() {
        LocalDate currentDate = LocalDate.now();
        LocalDate lastMonth = currentDate.minusMonths(3);

        List<Transaction> transactions = transactionDAO.findEntities(null, lastMonth, currentDate);

        HashMap<Category, CategoryQuantitySum> categoryMap = createCategoryMap(transactions);

        return createNextMonthExpenseDTO(categoryMap);

    }

    private NextMonthExpenseDTO createNextMonthExpenseDTO(HashMap<Category, CategoryQuantitySum> categoryMap) {
        NextMonthExpenseDTO nextMonthExpenseDTO = new NextMonthExpenseDTO();

        Double total = 0.0;

        for (Map.Entry<Category, CategoryQuantitySum> entry : categoryMap.entrySet()) {
            Category key = entry.getKey();
            CategoryQuantitySum value = entry.getValue();
            // Do something with key and value
            if (key == Category.HOUSING) {

                nextMonthExpenseDTO.setHousing(value.getTotalAmount() / value.getQuantity());
                total = total + nextMonthExpenseDTO.getHousing();

            }
            if (key == Category.TRAVEL) {

                nextMonthExpenseDTO.setTravel(value.getTotalAmount() / value.getQuantity());
                total = total + nextMonthExpenseDTO.getTravel();

            }
            if (key == Category.FOOD) {

                nextMonthExpenseDTO.setFood(value.getTotalAmount() / value.getQuantity());
                total = total + nextMonthExpenseDTO.getFood();

            }
            if (key == Category.UTILITIES) {

                nextMonthExpenseDTO.setUtilities(value.getTotalAmount() / value.getQuantity());
                total = total + nextMonthExpenseDTO.getUtilities();

            }
            if (key == Category.INSURANCE) {

                nextMonthExpenseDTO.setInsurance(value.getTotalAmount() / value.getQuantity());
                total = total + nextMonthExpenseDTO.getInsurance();

            }
            if (key == Category.HEALTHCARE) {

                nextMonthExpenseDTO.setHealthcare(value.getTotalAmount() / value.getQuantity());
                total = total + nextMonthExpenseDTO.getHealthcare();

            }
            if (key == Category.FINANCIAL) {

                nextMonthExpenseDTO.setFinancial(value.getTotalAmount() / value.getQuantity());
                total = total + nextMonthExpenseDTO.getFinancial();

            }
            if (key == Category.LIFESTYLE) {

                nextMonthExpenseDTO.setLifestyle(value.getTotalAmount() / value.getQuantity());
                total = total + nextMonthExpenseDTO.getLifestyle();

            }
            if (key == Category.ENTERTAINMENT) {

                nextMonthExpenseDTO.setEntertainment(value.getTotalAmount() / value.getQuantity());
                total = total + nextMonthExpenseDTO.getEntertainment();

            }
            if (key == Category.MISCELLANEOUS) {

                nextMonthExpenseDTO.setMiscellaneous(value.getTotalAmount() / value.getQuantity());
                total = total + nextMonthExpenseDTO.getMiscellaneous();

            }
        }

        nextMonthExpenseDTO.setTotal(total);
        return nextMonthExpenseDTO;
    }

    private HashMap<Category, CategoryQuantitySum> createCategoryMap(List<Transaction> transactions) {
        HashMap<Category, CategoryQuantitySum> categoryMap = new HashMap<>();
        for (Transaction transaction : transactions) {
            if (categoryMap.containsKey(transaction.getCategory())) {

                CategoryQuantitySum categoryQuantitySum = categoryMap.get(transaction.getCategory());
                categoryQuantitySum.addOneQuantity();
                categoryQuantitySum.addAmount(transaction.getAmount());

            } else {

                categoryMap.put(transaction.getCategory(), new CategoryQuantitySum(transaction.getAmount()));

            }
        }
        return categoryMap;
    }
}
