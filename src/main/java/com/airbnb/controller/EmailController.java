package com.airbnb.controller;

import com.airbnb.service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/emails")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/sendWelcome")
    public String sendWelcomeEmail(@RequestParam String email) {
        emailService.sendWelcomeEmail(email);
        return "Welcome email sent successfully";
    }
}
