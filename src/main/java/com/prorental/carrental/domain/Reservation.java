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
    //represent the car that is associated with this reservation. Now it makes sense.
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


//Reservation will look like this
//|   id  | car_id | user_id |   pick_up_time       |    drop_off_time      | pick_up_location | drop_off_location |    status    | total_price |
//        |-------|--------|---------|-----------------------|-----------------------|-----------------|-------------------|--------------|-------------|
//        |   1   |   101  |   201   | 2023-10-15 10:00:00  | 2023-10-18 15:30:00  |     Airport     |     Downtown      |  CONFIRMED   |   350.50    |
//        |   2   |   102  |   202   | 2023-10-16 14:30:00  | 2023-10-19 12:45:00  |   City Center   |     Beachfront    |  PENDING     |   275.00    |
//        |   3   |   103  |   203   | 2023-10-17 09:15:00  | 2023-10-19 10:00:00  |    Suburbia     |    Shopping Mall  |  CONFIRMED   |   420.75    |

//JsonView
//{
//  "id": 123,
//  "carId": {
//    "id": 456,
//    "make": "Toyota",
//    "model": "Camry",
//    "year": 2022
//  },
//  "userId": {
//    "id": 789,
//    "firstName": "John",
//    "lastName": "Doe",
//    "email": "john.doe@example.com"
//  },
//  "pickUpTime": "2023-10-15T10:00:00",
//  "dropOfTime": "2023-10-18T15:30:00",
//  "pickUpLocation": "Airport",
//  "dropOfLocation": "Downtown",
//  "status": "CONFIRMED",
//  "totalPrice": 350.50
//}
