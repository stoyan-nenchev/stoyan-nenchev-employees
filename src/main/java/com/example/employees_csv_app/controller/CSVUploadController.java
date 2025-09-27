package com.example.employees_csv_app.controller;

import com.example.employees_csv_app.dto.EmployeePairDTO;
import com.example.employees_csv_app.service.CSVProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/csv-upload")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CSVUploadController {

    private final CSVProcessingService csvProcessingService;

    @PostMapping("/employees")
    public ResponseEntity<List<EmployeePairDTO>> uploadEmployeesCSV(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(csvProcessingService.processCSV(file.getInputStream()));
    }
}
