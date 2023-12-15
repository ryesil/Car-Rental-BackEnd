package com.prorental.carrental.controller;


import com.prorental.carrental.domain.FileDB;
import com.prorental.carrental.dto.FileDTO;
import com.prorental.carrental.service.FileDBService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@CrossOrigin("http://localhost:8081")
@RequestMapping(path="/files")
public class FileController {

    private final FileDBService fileDBService;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file){
    try{
        FileDB fileDB = fileDBService.store(file);
        Map<String, String> map =new HashMap<>();
        map.put("imageId", fileDB.getId());
        return ResponseEntity.ok().body(map);
    } catch (IOException e) {

        Map<String, String> map = new HashMap<>();
        map.put("message", "Could not upload the file: "+ file.getOriginalFilename()+"!" );
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(map);
    }
    }


    // In the below code, We fetched all the files from the repo then converted them to fileDTO then sent
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FileDTO>> getAllFiles(){
        List<FileDTO> fileList = fileDBService.getAllFiles().map(dbFile-> {
            //Below we got the base URL and then the path to the fileController. Then added the fileId to it.
            //Then returned URIString. Clever
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/").path(dbFile.getId()).toUriString();
            return new FileDTO(dbFile.getName(), fileDownloadUri, dbFile.getType(), dbFile.getData().length);
        }).collect(Collectors.toList());
        return new ResponseEntity<>(fileList, HttpStatus.OK);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id){
        FileDB fileDB = fileDBService.getFile(id);
       //This return is a file. So we need to let the browser know that
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename= "+fileDB.getName()).body(fileDB.getData());
    }

    @GetMapping("/display/{id}")
    public ResponseEntity<byte[]> displayImage(@PathVariable String id){
        FileDB fileDB = fileDBService.getFile(id);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(fileDB.getData(),headers, HttpStatus.CREATED);
    }

}
