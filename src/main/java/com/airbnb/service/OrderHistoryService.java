package com.airbnb.service;

import com.airbnb.entity.OrderHistory;
import com.airbnb.repository.OrderHistoryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderHistoryService {

    @Autowired
    private OrderHistoryRepository orderHistoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public void saveOrUpdateOrderHistory(String username, String orderData) throws JsonProcessingException {
        Optional<OrderHistory> existingOrderHistoryOpt = orderHistoryRepository.findByUsername(username);
        OrderHistory orderHistory;

        if (existingOrderHistoryOpt.isPresent()) {
            orderHistory = existingOrderHistoryOpt.get();
            // Append new data to existing data
            String updatedOrderData = orderHistory.getOrderData() + "\n" + orderData;
            orderHistory.setOrderData(updatedOrderData);
        } else {
            orderHistory = new OrderHistory();
            orderHistory.setUsername(username);
            orderHistory.setOrderData(orderData);
        }

        orderHistoryRepository.save(orderHistory);
    }

    public Optional<OrderHistory> getOrderHistoryByUsername(String username) {
        return orderHistoryRepository.findByUsername(username);
    }
}
