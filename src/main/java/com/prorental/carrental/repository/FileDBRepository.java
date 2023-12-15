package com.prorental.carrental.repository;

import com.prorental.carrental.domain.FileDB;
import com.prorental.carrental.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Repository
@Transactional
public interface FileDBRepository extends JpaRepository<FileDB,String> {


   @Query("SELECT f FROM FileDB f WHERE f.id = ?1")
    Set<FileDB> getAllImagesById(@Param("id") String id) throws ResourceNotFoundException;

}
