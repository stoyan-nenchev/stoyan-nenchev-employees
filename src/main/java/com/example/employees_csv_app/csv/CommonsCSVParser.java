package com.example.employees_csv_app.csv;

import com.example.employees_csv_app.dto.CSVEmployeeRowDTO;
import com.example.employees_csv_app.enums.EmployeeCSVHeader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CommonsCSVParser implements CSVParser<CSVEmployeeRowDTO> {

    @Override
    public List<CSVEmployeeRowDTO> parse(InputStream inputStream) {
        List<CSVEmployeeRowDTO> employees = new ArrayList<>();

        try {
            Reader reader = new InputStreamReader(inputStream);
            CSVFormat format  = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setTrim(true)
                    .setNullString("NULL")
                    .get();

            org.apache.commons.csv.CSVParser parser = format.parse(reader);
            validateHeader(parser.getHeaderMap());

            for (CSVRecord parsedRecord : parser) {
                CSVEmployeeRowDTO employeeRow = CSVEmployeeRowDTO.builder()
                        .employeeId(parseInteger(parsedRecord.get(EmployeeCSVHeader.EmpID)))
                        .projectId(parseInteger(parsedRecord.get(EmployeeCSVHeader.ProjectID)))
                        .dateFrom(parseDate(parsedRecord.get(EmployeeCSVHeader.DateFrom)))
                        .dateTo(parseDate(parsedRecord.get(EmployeeCSVHeader.DateTo), true))
                        .build();

                employees.add(employeeRow);
            }

        } catch (IOException ioException) {
            throw new RuntimeException("Unable to parse CSV", ioException);
        }

        return employees;
    }

    private void validateHeader(Map<String, Integer> headerMap) {
        Set<String> requiredHeaders = Arrays.stream(EmployeeCSVHeader.values())
                .map(EmployeeCSVHeader::name)
                .collect(Collectors.toSet());

        Set<String> actualHeaders = new HashSet<>(headerMap.keySet());

        if (!actualHeaders.containsAll(requiredHeaders)) {
            Set<String> missing = new HashSet<>(requiredHeaders);
            missing.removeAll(actualHeaders);
            throw new IllegalArgumentException(String.format("Missing required headers %s", missing));
        }
    }

    private Integer parseInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(String.format("Invalid integer value %s", value));
        }

        return Integer.parseInt(value.trim());
    }

    private LocalDate parseDate(String value) {
        return parseDate(value, false);
    }

    private LocalDate parseDate(String value, boolean allowNullDate) {
        if (value == null || value.trim().isEmpty()) {
            if (allowNullDate) {
                return LocalDate.now();
            } else {
                throw new IllegalArgumentException(String.format("Invalid date value %s", value));
            }
        }

        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException dateTimeParseException) {
            throw new IllegalArgumentException(String.format("Invalid or not supported date value %s", value));
        }
    }
}
