package com.prorental.carrental.controller;

import com.prorental.carrental.domain.User;
import com.prorental.carrental.service.UserService;
import com.prorental.carrental.service.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
//@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

//3 ways to do autowiring
// we can add AllArgConstructor, or we can use AutoWired annotation or we can use constructor like below.
//@Autowired
//private UserService userService;
   private UserService userService;
   private ModelMapper modelMapper;
   public UserController(UserService userService, ModelMapper modelMapper){
       this.userService = userService;
       this.modelMapper = modelMapper;
   }


   //below will return everthing about the user. we don't want that.
@GetMapping("/auth/all")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<List<UserDTO>> getAllUsers(){
      List<User> userList =  userService.fetchAllUsers();
      List<UserDTO> userDTOList =  userList.stream().map(this::convertToDTO).collect(Collectors.toList());
      return new ResponseEntity<>(userDTOList, HttpStatus.OK);
}

//We convert User to UserDTO to hide sensitive information
private UserDTO convertToDTO(User user){
       UserDTO userDTO = modelMapper.map(user, UserDTO.class);
       return userDTO;
}

}