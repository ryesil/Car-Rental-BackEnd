package com.prorental.carrental.service;



import com.prorental.carrental.domain.FileDB;
import com.prorental.carrental.repository.FileDBRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class FileDBService {

    @Autowired
    FileDBRepository fileDBRepository;

    public FileDB store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        FileDB fileDB = new FileDB(fileName, file.getContentType(), file.getBytes());
        fileDBRepository.save(fileDB);
        return fileDB;
    }

    public Stream<FileDB> getAllFiles() {
        return fileDBRepository.findAll().stream();
    }

    public FileDB getFile(String id) {
        return fileDBRepository.findById(id).get();
    }
}
