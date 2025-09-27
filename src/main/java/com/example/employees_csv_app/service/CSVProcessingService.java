package com.example.employees_csv_app.service;

import com.example.employees_csv_app.csv.CSVParser;
import com.example.employees_csv_app.dto.CSVEmployeeRowDTO;
import com.example.employees_csv_app.dto.EmployeePairDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CSVProcessingService {

    private final CSVParser<CSVEmployeeRowDTO> csvParser;
    private final EmployeePairService employeePairService;

    public List<EmployeePairDTO> processCSV(InputStream inputStream) {
        List<CSVEmployeeRowDTO> parsedRows = csvParser.parse(inputStream);
        return employeePairService.findMostProjectsOverlapPair(parsedRows);
    }
}
