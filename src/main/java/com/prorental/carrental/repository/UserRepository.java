package com.prorental.carrental.repository;

import com.prorental.carrental.domain.User;
import com.prorental.carrental.exception.BadRequestException;
import com.prorental.carrental.exception.ConflictException;
import com.prorental.carrental.exception.ResourceNotFoundException;
import com.prorental.carrental.projection.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

     Optional<User> findByEmail(String email) throws ResourceNotFoundException;

     Boolean existsByEmail(String email) throws ConflictException;

     List<ProjectUser> findAllBy();

     //We user entity class name not the table name
    @Transactional
    @Modifying
    @Query("UPDATE User u "+
     "SET u.firstName=:firstName, u.lastName=:lastName, u.phoneNumber=:phoneNumber, u.email=:email, u.address=:address, u.zipCode=:zipCode WHERE u.id=:id"
     )

    void update(@Param("id") Long id
             , @Param("firstName") String firstName
             , @Param("lastName") String lastName
             , @Param("phoneNumber") String phoneNumber
             , @Param("email") String email
             , @Param("address") String address
             , @Param("zipCode") String zipCode
     ) throws BadRequestException;

    //It could be containing
    List<User> findByLastNameStartingWith(String lastName);
    List<User> findByLastNameContaining(String lastName);

}
