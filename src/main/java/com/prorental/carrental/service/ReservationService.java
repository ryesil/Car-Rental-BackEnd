package com.prorental.carrental.service;


import ch.qos.logback.core.encoder.EchoEncoder;
import com.prorental.carrental.domain.Car;
import com.prorental.carrental.domain.Reservation;
import com.prorental.carrental.domain.User;
import com.prorental.carrental.dto.ReservationDTO;
import com.prorental.carrental.enumaration.ReservationStatus;
import com.prorental.carrental.exception.BadRequestException;
import com.prorental.carrental.exception.ConflictException;
import com.prorental.carrental.exception.ResourceNotFoundException;
import com.prorental.carrental.repository.CarRepository;
import com.prorental.carrental.repository.ReservationRepository;
import com.prorental.carrental.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ReservationService {
     ReservationRepository reservationRepository;
     CarRepository carRepository;
     UserRepository userRepository;
     private final static String RESERVATION_NOT_FOUND_MSG = "reservation with id %d not found";
     private final static String USER_NOT_FOUND_MSG = "User with id %d not found";

     public List<ReservationDTO> fetchAllReservations() {
          List<ReservationDTO> reservationDTOS = reservationRepository.findAllReservations();
          return reservationDTOS;
     }

     public ReservationDTO getReservationById(Long id) throws ResourceNotFoundException {
          return reservationRepository.findReservationById(id).orElseThrow(()->new ResourceNotFoundException(String.format(RESERVATION_NOT_FOUND_MSG,id)));
     }

     public List<ReservationDTO> fetchUserReservationsById(Long id, Long userId) throws ResourceNotFoundException {
         return reservationRepository.findUserReservationsById(id, userId);
     }

     public ReservationDTO findByUserId(Long id, Long userId) throws ResourceNotFoundException{
          return reservationRepository.findReservationByUserId(id, userId);
     }

     public List<ReservationDTO> findAllByUserId(Long userId) throws ResourceNotFoundException{
          List<ReservationDTO> reservationDTOList = reservationRepository.findReservationsByUserId(userId);
          return reservationDTOList;
     }

     //We check the car if it is available==>
    //TODO we need to revisit the logic about being available
    //Then check if the user is in the system
     public void addReservation(Reservation reservation, Long userId, Car carId) throws BadRequestException {
         Boolean checkStatus  = carAvailability(carId.getId(), reservation.getPickUpTime(),reservation.getDropOffTime());
          User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG,userId)));
          //IN normal circumstances we get the carId from request param then get the car from repo to attach to the reservation.
         //In this API it looks like we get the car from pathParam. Interesting.
          if(!checkStatus){
              reservation.setStatus(ReservationStatus.CREATED);
         } else {
              throw new BadRequestException("Car is already reserved! Please choose another car");
         }
         reservation.setCarId(carId);
         reservation.setUserId(user);
         Double totalPrice = totalPrice(reservation.getPickUpTime(),reservation.getDropOffTime(),carId.getId());
         reservation.setTotalPrice(totalPrice);
         reservationRepository.save(reservation);
     }






     public Boolean carAvailability(Long carId, LocalDateTime pickUpTime, LocalDateTime dropOffTime){
          List<Reservation> checkStatus = reservationRepository.checkStatus(carId, pickUpTime, dropOffTime, ReservationStatus.DONE, ReservationStatus.CANCELLED);
          return checkStatus.size() > 0;
     }


     //Find the car then get the price of an hour
    //TODO move getTotalHours from Reservation to Util class
     public Double totalPrice(LocalDateTime pickUpTime, LocalDateTime dropOffTime, Long carId){
         Optional<Car> car = carRepository.findById(carId);
         Long hours = (new Reservation()).getTotalHours(pickUpTime, dropOffTime);
         return car.get().getPricePerHour()*hours;


     }

    public void updateReservation(Car carId, Long id, Reservation reservation) throws BadRequestException{
         boolean checkStatus = carAvailability(carId.getId(), reservation.getPickUpTime(),reservation.getDropOffTime());
         Reservation reservationFound = reservationRepository.findById(id).orElseThrow(()->new ConflictException("Error: Reservation does not exist!"));

         if(checkStatus){
             throw new BadRequestException("Car is already reserved! Please choose another");
         }
         Double totalPrice = totalPrice(reservation.getPickUpTime(),reservation.getDropOffTime(),carId.getId());
         reservationFound.setTotalPrice(totalPrice);
         reservationFound.setCarId(carId);
         reservationFound.setPickUpTime(reservation.getPickUpTime());
         reservationFound.setDropOffTime(reservationFound.getDropOffTime());
         reservationFound.setPickUpLocation(reservation.getDropOfLocation());
         reservationFound.setDropOfLocation(reservation.getDropOfLocation());
         reservationFound.setStatus(reservation.getStatus());
         reservationRepository.save(reservationFound);

    }

    public void removeById(Long id) throws ResourceNotFoundException{
         Boolean reservationExist = reservationRepository.existsById(id);
         if(!reservationExist){
             throw new ResourceNotFoundException("reservation doesn't exist");
         }
         reservationRepository.deleteById(id);
    }



}
