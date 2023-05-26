package com.prorental.carrental.service;

import com.prorental.carrental.domain.Message;
import com.prorental.carrental.exception.ResourceNotFoundException;
import com.prorental.carrental.repository.MessageRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

@Transactional//All or Nothing
@AllArgsConstructor
@Service
public class MessageService {

private MessageRepository messageRepository;

public Message createMessage(Message message){
    return messageRepository.save(message);
}

public Message getMessage(Long id){
    Message foundMessage = messageRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("There is no message with such id: "+id));
    return foundMessage;
}

    public List<Message> getAllMessages() {
    List<Message> allMessages=messageRepository.findAll();
    return allMessages;
    }


    public void deleteMessage(Long id) throws ResourceNotFoundException {
    messageRepository.deleteById(id);
    }


    public Message updateMessage(Long id, Message message) throws HttpServerErrorException.InternalServerError {
        Message foundMessage= getMessage(id);
        foundMessage.setSubject(message.getSubject());
        foundMessage.setBody(message.getBody());
        return messageRepository.save(message);
    }
}
