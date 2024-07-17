package com.airbnb.controller;

import com.airbnb.dto.OrderDTO;
import com.airbnb.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/placeOrder")
    public String placeOrder(@RequestBody OrderDTO orderDTO) {
        orderService.saveOrderAndSendEmail(orderDTO);
        return "Order placed successfully and email sent.";
    }
}
