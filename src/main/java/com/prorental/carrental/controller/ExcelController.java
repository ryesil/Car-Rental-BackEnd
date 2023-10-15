package com.prorental.carrental.controller;


import com.prorental.carrental.domain.User;
import com.prorental.carrental.repository.CarRepository;
import com.prorental.carrental.repository.ReservationRepository;
import com.prorental.carrental.repository.UserRepository;
import com.prorental.carrental.service.ExcelService;
import lombok.AllArgsConstructor;
import org.hibernate.service.spi.InjectService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.ByteArrayInputStream;
import java.util.List;

@CrossOrigin("https://localhost:8081")
@AllArgsConstructor
@RestController
@RequestMapping("/excel")
public class ExcelController {


    private ExcelService excelService;


    @GetMapping("/download/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InputStreamResource> getUserFile() {
        String fileName = "customers.xlsx";
        //InputStreamSource allowing one to read the underlying content stream multiple times
        InputStreamResource file = new InputStreamResource(excelService.loadUser());
        return ResponseEntity.ok()
                //Sets response headers for file download
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                //returns excel data wrapped in an InputStreamResource
                //The client's browser will prompt the user to download the file with the specified name, "customers.xlsx."
//                you're essentially telling the browser that the content you're sending
//                in the response should be treated as a downloadable attachment, and you suggest a filename for the downloaded file.
//                 When the user clicks to download the content, their browser will use the specified filename for the downloaded file.
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
    }


    @GetMapping("/download/cars")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InputStreamResource> getCarFile() {
        String fileName = "cars.xlsx";
        InputStreamResource file = new InputStreamResource(excelService.loadCars());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName).
                contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
    }

    @GetMapping("/download/reservations")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InputStreamResource> getReservationFile(){
        String fileName = "reservations.xlsx";
        InputStreamResource file = new InputStreamResource(excelService.loadReservations());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
    }


}
