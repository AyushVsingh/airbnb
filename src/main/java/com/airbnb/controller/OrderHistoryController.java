package com.airbnb.controller;

import com.airbnb.dto.OrderHistoryRequestDTO;
import com.airbnb.entity.OrderHistory;
import com.airbnb.service.OrderHistoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@RestController
@RequestMapping("/api/v1/orderhistory")
public class OrderHistoryController {

    @Autowired
    private OrderHistoryService orderHistoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/saveOrUpdate")
    public String saveOrUpdateOrderHistory(@RequestBody OrderHistoryRequestDTO requestDTO) {
        try {
            String orderDataJson = objectMapper.writeValueAsString(requestDTO.getOrderData());
            orderHistoryService.saveOrUpdateOrderHistory(requestDTO.getUsername(), orderDataJson);
            return "Order history updated successfully.";
        } catch (JsonProcessingException e) {
            return "Failed to process order data.";
        }
    }
    @GetMapping("/getByUsername/{username}")
    public Optional<OrderHistory> getOrderHistoryByUsername(@PathVariable String username) {
        return orderHistoryService.getOrderHistoryByUsername(username);
    }
}
