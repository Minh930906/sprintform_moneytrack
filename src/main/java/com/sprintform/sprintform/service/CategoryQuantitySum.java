package com.sprintform.sprintform.service;


public class CategoryQuantitySum {

    private Integer quantity;
    private Double totalAmount;

    public CategoryQuantitySum() {

    }

    public CategoryQuantitySum(Double totalAmount) {
        this.quantity = 1;
        this.totalAmount = totalAmount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void addOneQuantity() {
        this.quantity = quantity + 1;
    }

    public void addAmount(Double amount) {
        this.totalAmount = totalAmount + amount;
    }

}
