package com.prorental.carrental.controller;

import com.prorental.carrental.domain.User;
import com.prorental.carrental.service.UserService;
import com.prorental.carrental.service.dto.AdminDTO;
import com.prorental.carrental.service.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedNotifications;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

@GetMapping("/{id}/auth")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<UserDTO> getUSerByIdAdmin(@PathVariable Long id ){
       User user = userService.findById(id);
       UserDTO userDTO = modelMapper.map(user,UserDTO.class);
       return new ResponseEntity<>(userDTO, HttpStatus.OK);
}

@GetMapping
@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
public ResponseEntity<UserDTO> getUserById(HttpServletRequest request){
       //we need to set the id attribute to get it here. We set it in AuthTokenFilter
       Long id = (Long)request.getAttribute("id");
       User user = userService.findById(id);
       UserDTO userDTO = convertToDTO(user);
       return new ResponseEntity<>(userDTO, HttpStatus.OK);
}

//Admin can add a new User

@PostMapping("/add")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<Map<String, Boolean>> addUser(@Valid @RequestBody AdminDTO adminDTO){
       userService.addUserAuth(adminDTO);
Map<String,Boolean> map = new HashMap<>();
  map.put("User added successfully", true);
return new ResponseEntity<>(map, HttpStatus.OK);
}

//Admin or customer can make this request
@PutMapping
@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
public ResponseEntity<Map<String, Boolean>> updateUser(HttpServletRequest request, @Valid @RequestBody UserDTO userDTO){
       Long id = (Long)request.getAttribute("id");
       userService.updateUser(id, userDTO);
    Map<String,Boolean> map = new HashMap<>();
    map.put("User updated successfully", true);
    return new ResponseEntity<>(map, HttpStatus.OK);
}
//Below commented code is refactored using inner class
//    @PostMapping("/add")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<UserResponseMessage> addUser(@Valid @RequestBody AdminDTO adminDTO){
//        userService.addUserAuth(adminDTO);
//        return new ResponseEntity<>(new UserResponseMessage("User added successfully", true), HttpStatus.OK);
//    }


//We convert User to UserDTO to hide sensitive information
private UserDTO convertToDTO(User user){
       UserDTO userDTO = modelMapper.map(user, UserDTO.class);
       return userDTO;
}

//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//static class UserResponseMessage{
//private String message;
//private Boolean status;
//
//}

}