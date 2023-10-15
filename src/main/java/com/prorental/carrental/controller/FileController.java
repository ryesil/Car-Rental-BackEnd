package com.prorental.carrental.controller;


import com.prorental.carrental.repository.FileDBRepository;
import com.prorental.carrental.service.FileDBService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin("http://localhost:8081")
@RequestMapping(path="/files")
public class FileController {

    private final FileDBService fileDBService;




}
