package com.prorental.carrental.controller;

import com.prorental.carrental.domain.User;
import com.prorental.carrental.service.UserService;
import com.prorental.carrental.dto.AdminDTO;
import com.prorental.carrental.dto.UserDTO;
//import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {
//Important. For all requests, we get the token, then we validate it. Then we get the user id from the token. Then we check
// if the user exists in the database. Then we make a createUSernamePasswordAuth Token then we put it into the
// SecurityContextHolder then we forward it to the other area.


//3 ways to do autowiring
// we can add AllArgConstructor, or we can use AutoWired annotation or we can use constructor like below.
//@Autowired
//private UserService userService;
   private UserService userService;
   //private ModelMapper modelMapper;
//   public UserController(UserService userService, ModelMapper modelMapper){
//       this.userService = userService;
//       this.modelMapper = modelMapper;
//   }


   //below will return everything about the user. we don't want that. That's why we user userDTO.
    //Admin wants all users
@GetMapping("/auth/all")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<List<UserDTO>> getAllUsers(){
      List<User> userList =  userService.fetchAllUsers();
      List<UserDTO> userDTOList =  userList.stream().map(this::convertToDTO).collect(Collectors.toList());
      return new ResponseEntity<>(userDTOList, HttpStatus.OK);
}

//Admin wants a user
@GetMapping("/{id}/auth")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<UserDTO> getUSerByIdAdmin(@PathVariable Long id ){
       User user = userService.findById(id);
       UserDTO userDTO = modelMapper.map(user,UserDTO.class);
       return new ResponseEntity<>(userDTO, HttpStatus.OK);
}

//admin or user wants their own info
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
//This is for admin or customer to change their own information
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


//Below is for admin to change any user's information
@PutMapping("/{id}/auth")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<Map<String, Boolean>> updateUserAuth(@PathVariable  Long id, @Valid @RequestBody AdminDTO adminDTO){
       userService.updateUserAuth(id, adminDTO);
       Map<String, Boolean> map =new HashMap<>();
        map.put("User successfully updated", true);
       return new ResponseEntity<>(map, HttpStatus.OK);
}

@PatchMapping("/auth")
@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
public ResponseEntity<Map<String, Boolean>> updatePassword(HttpServletRequest request, @RequestBody Map<String, String> userMap){
       //when the user logins, we set id attribute into the request body. That's why we can get it anytime we want for any request from that user.
    Long id = (Long) request.getAttribute("id");
    String newPassword = userMap.get("newPassword");
    String oldPassword = userMap.get("oldPassword");
    userService.updatePassword(id, newPassword, oldPassword);
    Map<String, Boolean> map = new HashMap<>();
    map.put("Password changed successfully", true);
    return new ResponseEntity<>(map, HttpStatus.OK);
}

@DeleteMapping("/{id}/auth")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable Long id){
       userService.removeById(id);
    Map<String, Boolean> map = new HashMap<>();
    map.put("User deleted successfully", true);
    return new ResponseEntity<>(map, HttpStatus.OK);
}

//We filter in the database by lastName
//user/search?lastname=lastname
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>>  searchUserByLastName(@RequestParam("lastname") String lastName){
     List<User> userList = userService.searchUserByLastName(lastName);
     List<UserDTO> userListDTO = userList.stream().map(this::convertToDTO).collect(Collectors.toList());
       return new ResponseEntity<>(userListDTO,HttpStatus.OK);
    }

    @GetMapping("/search/contain")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>>  searchUserByLastNameContain(@RequestParam("lastname") String lastName){
        List<User> userList = userService.searchUserByLastNameContain(lastName);
        List<UserDTO> userListDTO = userList.stream().map(this::convertToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(userListDTO,HttpStatus.OK);
    }

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


















//***********************BELOW CODE IS REFACTORED ABOVE*************************************
//@RestController
//@RequestMapping("/user")
//public class UserController {
////Important. For all requests, we get the token, then we validate it. Then we get the user id from the token. Then we check
//// if the user exists in the database. Then we make a createUSernamePasswordAuth Token then we put it into the
//// SecurityContextHolder then we forward it to the other area.
//
//
////3 ways to do autowiring
//// we can add AllArgConstructor, or we can use AutoWired annotation or we can use constructor like below.
////@Autowired
////private UserService userService;
//   private UserService userService;
//   private ModelMapper modelMapper;
//   public UserController(UserService userService, ModelMapper modelMapper){
//       this.userService = userService;
//       this.modelMapper = modelMapper;
//   }
//
//
//   //below will return everything about the user. we don't want that. That's why we user userDTO.
//    //Admin wants all users
//@GetMapping("/auth/all")
//@PreAuthorize("hasRole('ADMIN')")
//public ResponseEntity<List<UserDTO>> getAllUsers(){
//      List<User> userList =  userService.fetchAllUsers();
//      List<UserDTO> userDTOList =  userList.stream().map(this::convertToDTO).collect(Collectors.toList());
//      return new ResponseEntity<>(userDTOList, HttpStatus.OK);
//}
//
////Admin wants a user
//@GetMapping("/{id}/auth")
//@PreAuthorize("hasRole('ADMIN')")
//public ResponseEntity<UserDTO> getUSerByIdAdmin(@PathVariable Long id ){
//       User user = userService.findById(id);
//       UserDTO userDTO = modelMapper.map(user,UserDTO.class);
//       return new ResponseEntity<>(userDTO, HttpStatus.OK);
//}
//
////admin or user wants their own info
//@GetMapping
//@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
//public ResponseEntity<UserDTO> getUserById(HttpServletRequest request){
//       //we need to set the id attribute to get it here. We set it in AuthTokenFilter
//       Long id = (Long)request.getAttribute("id");
//       User user = userService.findById(id);
//       UserDTO userDTO = convertToDTO(user);
//       return new ResponseEntity<>(userDTO, HttpStatus.OK);
//}
//
////Admin can add a new User
//
//@PostMapping("/add")
//@PreAuthorize("hasRole('ADMIN')")
//public ResponseEntity<Map<String, Boolean>> addUser(@Valid @RequestBody AdminDTO adminDTO){
//       userService.addUserAuth(adminDTO);
//Map<String,Boolean> map = new HashMap<>();
//  map.put("User added successfully", true);
//return new ResponseEntity<>(map, HttpStatus.OK);
//}
//
////Admin or customer can make this request
////This is for admin or customer to change their own information
//@PutMapping
//@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
//public ResponseEntity<Map<String, Boolean>> updateUser(HttpServletRequest request, @Valid @RequestBody UserDTO userDTO){
//       Long id = (Long)request.getAttribute("id");
//       userService.updateUser(id, userDTO);
//    Map<String,Boolean> map = new HashMap<>();
//    map.put("User updated successfully", true);
//    return new ResponseEntity<>(map, HttpStatus.OK);
//}
////Below commented code is refactored using inner class
////    @PostMapping("/add")
////    @PreAuthorize("hasRole('ADMIN')")
////    public ResponseEntity<UserResponseMessage> addUser(@Valid @RequestBody AdminDTO adminDTO){
////        userService.addUserAuth(adminDTO);
////        return new ResponseEntity<>(new UserResponseMessage("User added successfully", true), HttpStatus.OK);
////    }
//
//
////Below is for admin to change any user's information
//@PutMapping("/{id}/auth")
//@PreAuthorize("hasRole('ADMIN')")
//public ResponseEntity<Map<String, Boolean>> updateUserAuth(@PathVariable  Long id, @Valid @RequestBody AdminDTO adminDTO){
//       userService.updateUserAuth(id, adminDTO);
//       Map<String, Boolean> map =new HashMap<>();
//        map.put("User successfully updated", true);
//       return new ResponseEntity<>(map, HttpStatus.OK);
//}
//
//@PatchMapping("/auth")
//@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
//public ResponseEntity<Map<String, Boolean>> updatePassword(HttpServletRequest request, @RequestBody Map<String, String> userMap){
//       //when the user logins, we set id attribute into the request body. That's why we can get it anytime we want for any request from that user.
//    Long id = (Long) request.getAttribute("id");
//    String newPassword = userMap.get("newPassword");
//    String oldPassword = userMap.get("oldPassword");
//    userService.updatePassword(id, newPassword, oldPassword);
//    Map<String, Boolean> map = new HashMap<>();
//    map.put("Password changed successfully", true);
//    return new ResponseEntity<>(map, HttpStatus.OK);
//}
//
//@DeleteMapping("/{id}/auth")
//@PreAuthorize("hasRole('ADMIN')")
//public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable Long id){
//       userService.removeById(id);
//    Map<String, Boolean> map = new HashMap<>();
//    map.put("User deleted successfully", true);
//    return new ResponseEntity<>(map, HttpStatus.OK);
//}
//
////We filter in the database by lastName
////user/search?lastname=lastname
//    @GetMapping("/search")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<List<UserDTO>>  searchUserByLastName(@RequestParam("lastname") String lastName){
//     List<User> userList = userService.searchUserByLastName(lastName);
//     List<UserDTO> userListDTO = userList.stream().map(this::convertToDTO).collect(Collectors.toList());
//       return new ResponseEntity<>(userListDTO,HttpStatus.OK);
//    }
//
//    @GetMapping("/search/contain")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<List<UserDTO>>  searchUserByLastNameContain(@RequestParam("lastname") String lastName){
//        List<User> userList = userService.searchUserByLastNameContain(lastName);
//        List<UserDTO> userListDTO = userList.stream().map(this::convertToDTO).collect(Collectors.toList());
//        return new ResponseEntity<>(userListDTO,HttpStatus.OK);
//    }
//
////We convert User to UserDTO to hide sensitive information
//private UserDTO convertToDTO(User user){
//       UserDTO userDTO = modelMapper.map(user, UserDTO.class);
//       return userDTO;
//}
//
////@Getter
////@Setter
////@AllArgsConstructor
////@NoArgsConstructor
////static class UserResponseMessage{
////private String message;
////private Boolean status;
////
////}
//
//}