package com.prorental.carrental.controller;


import com.prorental.carrental.domain.Message;
import com.prorental.carrental.service.MessageService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/message")
public class MessageController {


    private static Logger logger = LoggerFactory.getLogger(MessageController.class);
    //If we add the messageService in the constructor then we don't need to autowired.
    //For that we can use @AllConstructor annotation
    //Constructor autowired yerine kullanilabilir
//@Autowired
    private MessageService messageService;


//public MessageController(MessageService messageService){
//    this.messageService=messageService;
//}

    @PostMapping
    public ResponseEntity<Message> createMessage(@Valid @RequestBody Message message) {

        Message savedMessage = messageService.createMessage(message);

        return new ResponseEntity<>(savedMessage, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessage(@PathVariable Long id) {
        Message foundMessage = messageService.getMessage(id);
        return ResponseEntity.ok(foundMessage);
    }


    //http://localhost:8080/car-rental/api/message/request?id=1
    //This is similar to the above method. This one is with request.
    @GetMapping("request")
    public Message getMessageByRequest(@RequestParam Long id) {
        return messageService.getMessage(id);
    }


    @GetMapping
    public ResponseEntity<List<Message>> getAllMessage() {
        List<Message> messagesList= messageService.getAllMessages();
        return ResponseEntity.ok(messagesList);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,String>> deleteMessage(@PathVariable Long id){
        //Logging
        logger.info("Requested Id [{}]", id.longValue());
        messageService.deleteMessage(id);
        Map<String,String> map= new HashMap<>();
        map.put("Success", String.valueOf(true));
        map.put("id",String.valueOf(id.longValue()));
        return  new ResponseEntity<>(map,HttpStatus.OK);
    }

@PutMapping("/{id}")
    public ResponseEntity<Message> updateMessage(@Valid @PathVariable Long id, @RequestBody Message message){
        return new ResponseEntity<>(messageService.updateMessage(id, message),HttpStatus.OK);
}



}
