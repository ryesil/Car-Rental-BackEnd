package com.prorental.carrental.repository;

import com.prorental.carrental.domain.Car;
import com.prorental.carrental.dto.CarDTO;
import com.prorental.carrental.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface CarRepository extends JpaRepository<Car,Long> {

    //This is again for All cars. We get the cars from database and convert them to CarDTO
    //CarDTO has a constructor that converts Car to CarDTO
    @Transactional
    @Query("SELECT new com.prorental.carrental.dto.CarDTO(c) FROM Car c")
    List<CarDTO> findAllCars();

    @Transactional
    @Query("SELECT new com.prorental.carrental.dto.CarDTO(c) FROM Car c WHERE c.id = ?1")
    Optional<CarDTO> findCarByIdx(Long id) throws ResourceNotFoundException;


    //This fetch will bring all the data related to the car.
    // fetch will do an eager fetch that means images will be fetched even though we don't access them or don't want them
    //This will assure all transactions will be done in one query not in multiple queries.
    //c.image means we are accessing the images of c
    //Ok Below code will find the car and related images
    @Transactional
    @Query("SELECT c FROM Car c LEFT JOIN FETCH c.image img WHERE c.id =: carId")
    Optional<Car> findCarBy(@Param("id") Long carId) throws ResourceNotFoundException;
    //Example
    //SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.orderItems oi
    //In this case, it retrieves all orders and eagerly loads their
    // associated order items. This results in a single query that
    // fetches both orders and their order items.

}
