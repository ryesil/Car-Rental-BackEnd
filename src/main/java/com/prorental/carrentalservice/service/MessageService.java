package com.prorental.carrentalservice.service;

import com.prorental.carrentalservice.domain.Message;
import com.prorental.carrentalservice.repository.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class MessageService {

private MessageRepository messageRepository;

public Message createMessage(Message message){
    return messageRepository.save(message);
}

}
