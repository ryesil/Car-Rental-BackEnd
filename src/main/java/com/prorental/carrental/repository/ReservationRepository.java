package com.prorental.carrental.repository;

import com.prorental.carrental.domain.Car;
import com.prorental.carrental.domain.Reservation;
import com.prorental.carrental.domain.User;
import com.prorental.carrental.dto.ReservationDTO;
import com.prorental.carrental.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    boolean existsByUserId(User user);

    boolean existsByCarId(Car car);

    @Transactional
    @Query("SELECT CASE WHEN COUNT(r)>0 THEN true ELSE false END FROM Reservation r WHERE r.carId =?1")
    Boolean existsByCarIdx(@Param("car") Car car);

    @Transactional
    @Query("SELECT new com.prorental.carrental.dto.ReservationDTO(r) FROM Reservation r")
    List<ReservationDTO> findAllReservations();


    @Transactional
    @Query("SELECT new com.prorental.carrental.dto.ReservationDTO(r) FROM Reservation r LEFT JOIN FETCH r.carId carId WHERE r.id =: id")
   Optional<ReservationDTO> findUserReservationsById(@Param("id") String id) throws ResourceNotFoundException;

}
