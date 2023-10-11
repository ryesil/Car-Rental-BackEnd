package com.prorental.carrental.service;

import com.prorental.carrental.domain.Car;
import com.prorental.carrental.domain.FileDB;
import com.prorental.carrental.domain.User;
import com.prorental.carrental.dto.CarDTO;
import com.prorental.carrental.exception.BadRequestException;
import com.prorental.carrental.exception.ResourceNotFoundException;
import com.prorental.carrental.repository.CarRepository;
import com.prorental.carrental.repository.FileDBRepository;
import com.prorental.carrental.repository.ReservationRepository;
import com.prorental.carrental.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.*;

@AllArgsConstructor
@Service
public class CarService {

    private CarRepository carRepository;
    private UserRepository userRepository;
    private FileDBRepository fileDBRepository;
    private ReservationRepository reservationRepository;
    private final static String CAR_NOT_FOUND_MSG = "car with id %d not found";

    private final static String IMAGE_NOT_FOUND_MSG = "car with id %s not found";

   public CarDTO getCarById(Long id){
       //In the carRepository we defined a select statement that returns CarDTO
     return carRepository.findCarByIdx(id).orElseThrow(()->new ResourceNotFoundException(String.format(CAR_NOT_FOUND_MSG,id)));
   }


    public List<CarDTO> fetchAllCars() {
       return carRepository.findAllCars();
    }


    public void add(Car car, String imageId) throws BadRequestException {
        FileDB fileDb = fileDBRepository.findById(imageId).orElseThrow(()-> new ResourceNotFoundException(String.format(IMAGE_NOT_FOUND_MSG,imageId)));
        Set<FileDB> fileDBs = new HashSet<>();
        fileDBs.add(fileDb);
        car.setImage(fileDBs);
        car.setBuiltIn(false);
        carRepository.save(car);
    }

    //TODO Get back here and redo the carUpdate
    //Careful
    //If car is builtIn then we don't update
    public void updateCar(Long id, Car car, String imageId){
       //Below findCarById will return car and related images. Return will look like
        //Look at images
        //Car entity:
        //- ID: 1
        //- Name: Car A
        //- Images:
        //  - Image 1:
        //    - Image ID: 101
        //    - Image URL: car1_image1.jpg
        //  - Image 2:
        //    - Image ID: 102
        //    - Image URL: car1_image2.jpg

       Car foundCar = carRepository.findCarBy(id).orElseThrow(()->new ResourceNotFoundException("No Car Found"));
       FileDB images = fileDBRepository.findById(imageId).get();
       if(foundCar.getBuiltIn()){
           throw new BadRequestException("You don't have permission to update the car!");
       }
       car.setBuiltIn(false);
       Set<FileDB> set = new HashSet<>();
        foundCar = car;
        set.add(images);
        foundCar.setImage(set);
        carRepository.save(foundCar);
    }

    //If car is builtIn you cannot remove
    //If car has a reservation you cannot remove
    public void removeById(Long id){
       Car car = carRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("There is no such car"));

       if(car.getBuiltIn()){
           throw new BadRequestException("You dont have permission to delete car!");
       }

       if(reservationRepository.existsByCarId(car)){
           throw new BadRequestException("Reservation(s) exist for the car!");
       }

       carRepository.deleteById(id);
    }

}
