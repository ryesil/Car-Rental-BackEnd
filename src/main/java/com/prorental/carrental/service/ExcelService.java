package com.prorental.carrental.service;


import com.prorental.carrental.domain.Car;
import com.prorental.carrental.domain.Reservation;
import com.prorental.carrental.domain.User;
import com.prorental.carrental.helper.ExcelHelper;
import com.prorental.carrental.repository.CarRepository;
import com.prorental.carrental.repository.ReservationRepository;
import com.prorental.carrental.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ExcelService {

    UserRepository userRepository;
    CarRepository carRepository;
    ReservationRepository reservationRepository;

    public ByteArrayInputStream loadUser(){
        List<User> users = userRepository.findAll();
        return ExcelHelper.usersExcel(users);
    }


    public ByteArrayInputStream loadCars(){
        List<Car> cars = carRepository.findAll();
        return ExcelHelper.carsExcel(cars);
    }

    public ByteArrayInputStream loadReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return ExcelHelper.reservationExcel(reservations);
    }
}
