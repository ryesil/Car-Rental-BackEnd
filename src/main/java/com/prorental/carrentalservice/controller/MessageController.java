package com.prorental.carrentalservice.controller;


import com.prorental.carrentalservice.domain.Message;
import com.prorental.carrentalservice.service.MessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("")
public class MessageController {


    private static Logger logger= LoggerFactory.getLogger(MessageController.class);
//If we add the messageService in the constructor then we don't need to autowired.
    //For that we can use @AllConstructor annotation
    //Constructor autowired yerine kullanilabilir
//@Autowired
private MessageService messageService;



//public MessageController(MessageService messageService){
//    this.messageService=messageService;
//}

@PostMapping("/message")
    public ResponseEntity<Message> createMessage(@Valid @RequestBody Message message){

    Message savedMessage = messageService.createMessage(message);

    return new ResponseEntity<>(savedMessage, HttpStatus.CREATED);
}

@GetMapping("/message/{id}")
public ResponseEntity<Message> getMessage(@PathVariable Long id){
    Message foundMessage = messageService.getMessage(id);
    return ResponseEntity.ok(foundMessage);
}

}
