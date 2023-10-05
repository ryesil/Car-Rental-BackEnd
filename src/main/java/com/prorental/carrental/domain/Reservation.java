package com.prorental.carrental.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.prorental.carrental.enumaration.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Here we have a car_id FK column that takes the PK id of Car and adds it to this table as FK.
    //This is the entire car. We are using carId to reach it. In this table we have the carID that
    //represent the car that is associated with this reservation.
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private Car carId;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="user_id",referencedColumnName="id")
    private User userId;


    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "MM/dd/yyyy HH:mm:ss", timezone = "US/Central")
    @NotNull(message = "Please enter the pick up time of the reservation")
    @Column(nullable = false)
    private LocalDateTime pickUpTime;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="MM/dd/yyyy HH:mm:ss", timezone = "US/Central")
    @NotNull(message = "Please enter the drop up time of the reservation")
    @Column(nullable = false)
    private LocalDateTime dropOfTime;

    @Column(length = 50, nullable = false)
    @NotNull(message = "Please enter the pick up location of the reservation")
    private String pickUpLocation;

    @Column(length = 50, nullable = false)
    @NotNull(message = "Please enter the drop of location of the reservation")
    private String dropOfLocation;


    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private ReservationStatus status;

    @Column(nullable = false)
    private Double totalPrice;


    public Long getTotalHours(LocalDateTime pickUpTime, LocalDateTime dropOfTime) {
//        Duration duration = Duration.between(pickUpTime,dropOfTime);
//        return duration.toHours();
        return ChronoUnit.HOURS.between(pickUpTime,dropOfTime);
    }
}
