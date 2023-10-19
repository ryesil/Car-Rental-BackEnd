package com.prorental.carrental.controller;

import com.prorental.carrental.domain.Car;
import com.prorental.carrental.dto.CarDTO;
import com.prorental.carrental.service.CarService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//If we want to restict the origin we can do like
//@CrossOrigin(origins = "https://www.trusted-bookstore-app.com")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@RestController
@Produces(MediaType.APPLICATION_JSON)
@RequestMapping("/car")
public class CarController {

    public CarService carService;

    @GetMapping("visitors/all")
    public ResponseEntity<List<CarDTO>> getAllCars(){
        List<CarDTO> cars = carService.fetchAllCars();
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    @GetMapping("/visitors/{id}")
    public ResponseEntity<CarDTO> getCarById(@PathVariable Long id){
        CarDTO car = carService.getCarById(id);
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

// We return conformation and boolean
//Car doesn't have the images. images come in the url
    @PostMapping("/admin/{id}/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> addCar(@PathVariable String id, @Valid @RequestBody Car car){
        carService.add(car,id);
        Map<String, Boolean> map = new HashMap<>();
        map.put("Car added successfully!", true);
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }



    @PutMapping("/admin/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> updateCar(@RequestParam("id") Long id, @RequestParam("imageId") String imageId, @Valid @RequestBody Car car){

        carService.updateCar(id, car, imageId);
        Map<String, Boolean> map = new HashMap<>();
            map.put("Car updated successfully!", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/admin/{id}/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> deleteCar(@PathVariable Long id){
        carService.removeById(id);
        Map<String, Boolean> map = new HashMap<>();
        map.put("Car deleted successfully!", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


}
