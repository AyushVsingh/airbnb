package com.airbnb.controller;

import com.airbnb.dto.MessageDto;
import com.airbnb.entity.Message;
import com.airbnb.repository.MessageRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    private final MessageRepository messageRepository;

    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @PostMapping("/addMessage")
    public ResponseEntity<String> addMessage(@RequestBody MessageDto messageDto) {
        Message message = new Message();
        message.setName(messageDto.getName());
        message.setEmail(messageDto.getEmail());
        message.setMessage(messageDto.getMessage());

        messageRepository.save(message);

        return new ResponseEntity<>("Message saved successfully", HttpStatus.CREATED);
    }
}
