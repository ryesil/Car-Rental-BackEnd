package com.prorental.carrental.service;

import com.prorental.carrental.domain.Role;
import com.prorental.carrental.domain.User;
import com.prorental.carrental.enumaration.UserRole;
import com.prorental.carrental.exception.BadRequestException;
import com.prorental.carrental.exception.ConflictException;
import com.prorental.carrental.exception.ResourceNotFoundException;
import com.prorental.carrental.repository.RoleRepository;
import com.prorental.carrental.repository.UserRepository;
import com.prorental.carrental.service.dto.AdminDTO;
import com.prorental.carrental.service.dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
@Service
@AllArgsConstructor
public class UserService {

    //We use String.format
    private final static String USER_NOT_FOUND_MSG = "user with id %d not found";
    //To register we need the userRepo;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    public void register(User user) throws ConflictException {
    if(userRepository.existsByEmail(user.getEmail())){
        throw new ConflictException("Error: Email is already in use");
    } else {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setBuiltIn(false);
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(UserRole.ROLE_CUSTOMER).orElseThrow(()-> new ResourceNotFoundException("Role Not Found"));
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
    }

    }
    public List<User> fetchAllUsers(){
        return userRepository.findAll();
    }

    public User findById(Long id) throws ResourceNotFoundException{
     User user =  userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG,id)));
    return user;
    }

    //We receive a user info
public void addUserAuth(AdminDTO adminDTO) throws BadRequestException {
       Boolean emailExists = userRepository.existsByEmail(adminDTO.getEmail());
       if(emailExists) {
           throw new ConflictException("Error: Email is already in use");
       }
       //After we check for the email. If the email doesn't exist then we can register the user
       //Since the user's password will be encoded if the user register himself we will encode the coming password from the admin
       String encodedPassWord = passwordEncoder.encode(adminDTO.getPassword());
        adminDTO.setPassword(encodedPassWord);
        adminDTO.setBuiltIn(false);

        //So this is incoming data, and it comes as a String we will convert it to Role.
        Set<String> userRoles = adminDTO.getRoles();
        Set<Role> roles = addRoles(userRoles);
      User user = new User(adminDTO.getFirstName(), adminDTO.getLastName(), adminDTO.getPassword(),
              adminDTO.getPhoneNumber(), adminDTO.getEmail(), adminDTO.getAddress(), adminDTO.getZipCode(), roles, adminDTO.getBuiltIn()
              );
      userRepository.save(user);
}

//Update AdminUser
    public void  updateUser(Long id, UserDTO userDTO) throws BadRequestException{
      boolean emailExists =  userRepository.existsByEmail(userDTO.getEmail());
      Optional<User> foundUser =  userRepository.findById(id);
      //we have set the builtIn false at default. If the user is builtIn then we cannot change that user.
        if(foundUser.get().getBuiltIn()){
            throw new ResourceNotFoundException("You don't have permission to change this user info");
        }

        //if the user's email in the database doesn't match the incoming email
        if(emailExists && !userDTO.getEmail().equals(foundUser.get().getEmail())){
            throw new ConflictException("Error: Email is in use");
        }

        //UserRepository doesn't have update. We gotto make it.
        userRepository.update(id, userDTO.getFirstName(),
                userDTO.getLastName(), userDTO.getPhoneNumber(),
                userDTO.getEmail(), userDTO.getAddress(), userDTO.getZipCode());
    }

   public void updateUserAuth(Long id, AdminDTO adminDTO) throws BadRequestException{
        Boolean emailExists = userRepository.existsByEmail(adminDTO.getEmail());
       Optional<User> foundUser = userRepository.findById(id);

       if(foundUser.get().getBuiltIn()){
           throw new ResourceNotFoundException("You don't have permission to change this user info");
       }
       adminDTO.setBuiltIn(false);

       if(emailExists && !foundUser.get().getEmail().equals(adminDTO.getEmail())){
           throw new ConflictException("Eror: Email is in use");
       }
        if(adminDTO.getPassword() == null){
            adminDTO.setPassword(foundUser.get().getPassword());
        } else {
            String encodedPassword = passwordEncoder.encode(adminDTO.getPassword());
            adminDTO.setPassword(encodedPassword);
        }
        //Roles will come as string, and we convert them to Roles to save to the database.
        Set<String> userRoles = adminDTO.getRoles();
        Set<Role> roles = addRoles(userRoles);

        User user =new User(id, adminDTO.getFirstName(), adminDTO.getLastName(), adminDTO.getEmail(), adminDTO.getPassword(),
                adminDTO.getPhoneNumber(), adminDTO.getAddress(), adminDTO.getZipCode(), adminDTO.getBuiltIn(), roles
                );
            userRepository.save(user);
   }


 //My version of updatePassword (working :))
//   public void updatePassword(Long id, String oldPassword, String newPassword){
//    Boolean userExists = userRepository.existsById(id);
//    Boolean newPasswordChecker = newPassword.length() <=60 && newPassword.length() >=4;
//    if(!userExists || !newPasswordChecker){
//        throw new BadRequestException("User not found or new password doesn't meet requirements");
//    } else {
//        String encodedPassword = passwordEncoder.encode(newPassword);
//        Optional<User> user = userRepository.findById(id);
//        user.get().setPassword(encodedPassword);
//        userRepository.save(user.get());
//    }
//   }
public void updatePassword(Long id, String newPassword, String oldPassword){
        User user = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("user not found with id "+ id));
        //if default user, then we don't want to change it
        if(user.getBuiltIn()){
            throw new ResourceNotFoundException("You don't have permission to update password");
        }
        //we got to check the coming oldPassword with the user's password in the database.
        //We compare the hashed passwords.
    // Below haspw gets the salt of the user.getPassword and applies it to oldPassword to produce a hashed password.
    //Then compares this new hashed password with the hashed password from the database.
        if(!(BCrypt.hashpw(oldPassword, user.getPassword())).equals(user.getPassword())){
            throw new BadRequestException("Your password doesn't match");
        }
        //we will update the user's password then save it to database.
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
}

public void removeById(Long id) throws ResourceNotFoundException{
      User user = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG,id)));
      if(user.getBuiltIn()){
          throw new BadRequestException("You don't have permission to delete ");
      }
      userRepository.deleteById(id);

    }

public Set<Role> addRoles(Set<String> userRoles){
Set<Role> roles = new HashSet<>();

if(userRoles == null){
    Role userRole = roleRepository.findByName(UserRole.ROLE_CUSTOMER)
            .orElseThrow(()->new RuntimeException("Error: Role not Found"));
    roles.add(userRole);
} else {
userRoles.forEach(role->{
    switch (role){
        case "Administrator":
            Role adminRole = roleRepository.findByName(UserRole.ROLE_ADMIN)
                    .orElseThrow(()->new RuntimeException("Error: Role not found"));
            roles.add(adminRole);

            break;

        case "Manager":
            Role managerRole = roleRepository.findByName(UserRole.ROLE_MANAGER)
                    .orElseThrow(()->new RuntimeException("Error: Role not found"));
            roles.add(managerRole);

            break;

        default:
            Role customerRole = roleRepository.findByName(UserRole.ROLE_CUSTOMER)
                    .orElseThrow(()->new RuntimeException("Error: Role not found"));
            roles.add(customerRole);
    }
});
}
return roles;
}


}
