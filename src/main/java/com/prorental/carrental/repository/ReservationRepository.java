package com.prorental.carrental.repository;

import com.prorental.carrental.domain.Car;
import com.prorental.carrental.domain.Reservation;
import com.prorental.carrental.domain.User;
import com.prorental.carrental.dto.ReservationDTO;
import com.prorental.carrental.enumaration.ReservationStatus;
import com.prorental.carrental.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    @Query("SELECT new com.prorental.carrental.dto.ReservationDTO(r) FROM Reservation r  WHERE r.id =?1 AND r.userId=?2")
    List<ReservationDTO> findUserReservationsById(@Param("id") Long id, @Param("userId") Long userId) throws ResourceNotFoundException;

    @Transactional
    @Query("SELECT new com.prorental.carrental.dto.ReservationDTO(r) FROM Reservation r  WHERE r.id =?1")
    Optional<ReservationDTO> findReservationById(@Param("id") Long id);

    @Transactional
    @Query("SELECT new com.prorental.carrental.dto.ReservationDTO(r) FROM Reservation r WHERE r.id=?1 AND r.userId.id=?2")//here userId represent the user class. So we need its id
    ReservationDTO findReservationByUserId(Long id, Long userId);

    @Transactional
    @Query("SELECT new com.prorental.carrental.dto.ReservationDTO(r) FROM Reservation r WHERE r.userId.id =: userId")
    List<ReservationDTO> findReservationsByUserId(Long userId);

    @Transactional
    @Query("SELECT r FROM Reservation r "+
            "LEFT JOIN fetch r.carId cd "+
            "LEFT JOIN fetch cd.image img"+
            "LEFT JOIN fetch r.userId ud WHERE "+
            "cd.id=?1 AND r.status<>?4 AND r.status<>?5 AND ((?2 BETWEEN r.pickUpTime and r.dropOffTime) OR (?3 BETWEEN r.pickUpTime and r.dropOffTime))")
    List<Reservation> checkStatus(Long carId, LocalDateTime pickUpTime, LocalDateTime dropOffTime, ReservationStatus done, ReservationStatus cancelled);
}
