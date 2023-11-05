package com.sprintform.sprintform.repository;

import com.sprintform.sprintform.dto.Category;
import com.sprintform.sprintform.entity.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class TransactionDAO {

    @Autowired
    private final EntityManager entityManager;

    public List<Transaction> findEntities(Category category, LocalDate startDate, LocalDate endDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Transaction> query = cb.createQuery(Transaction.class);
        Root<Transaction> root = query.from(Transaction.class);

        Predicate criteria = cb.conjunction(); // The initial empty predicate

        if (category != null) {
            criteria = cb.and(criteria, cb.equal(root.get("category"), category));
        }

        if (startDate != null) {
            criteria = cb.and(criteria, cb.greaterThanOrEqualTo(root.get("date"), startDate));
        }

        if (endDate != null) {
            criteria = cb.and(criteria, cb.lessThanOrEqualTo(root.get("date"), endDate));
        }


        query.where(criteria);

        return entityManager.createQuery(query).getResultList();
    }

}
