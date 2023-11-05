package com.sprintform.sprintform.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestTransactionDTO {

    @NotNull
    private Double amount;
    @NotNull
    private String description;
    @NotNull
    private Category category;
    @NotNull
    private LocalDate date;
}
