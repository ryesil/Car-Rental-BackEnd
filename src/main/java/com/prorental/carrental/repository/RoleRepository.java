package com.prorental.carrental.repository;

import com.prorental.carrental.domain.Role;
import com.prorental.carrental.enumaration.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(UserRole name);

}
