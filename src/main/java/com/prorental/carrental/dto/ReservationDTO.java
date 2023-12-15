package com.prorental.carrental.dto;


import com.prorental.carrental.domain.Reservation;
import com.prorental.carrental.enumaration.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    private Long id;

    private CarDTO car;

    private Long userId;

    private LocalDateTime pickUpTime;

    private LocalDateTime dropOffTime;

    private String pickUpLocation;

    private String dropOfLocation;

    private ReservationStatus status;

    private Double totalPrice;

    public ReservationDTO(Reservation reservation){
        this.id = reservation.getId();
        this.car = new CarDTO(reservation.getCarId());
        this.userId = reservation.getUserId().getId();
        this.pickUpTime = reservation.getPickUpTime();
        this.dropOffTime = reservation.getDropOffTime();
        this.pickUpLocation = reservation.getPickUpLocation();
        this.dropOfLocation = reservation.getDropOfLocation();
        this.status = reservation.getStatus();
        this.totalPrice = reservation.getTotalPrice();
    }

}
