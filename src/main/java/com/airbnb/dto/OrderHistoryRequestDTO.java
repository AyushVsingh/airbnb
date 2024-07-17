package com.airbnb.dto;

import java.util.List;

public class OrderHistoryRequestDTO {
    private String username;
    private List<Object> orderData;

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Object> getOrderData() {
        return orderData;
    }

    public void setOrderData(List<Object> orderData) {
        this.orderData = orderData;
    }
}
