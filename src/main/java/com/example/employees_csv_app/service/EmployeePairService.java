package com.example.employees_csv_app.service;

import com.example.employees_csv_app.dto.CSVEmployeeRowDTO;
import com.example.employees_csv_app.dto.EmployeePairDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeePairService {

    public List<EmployeePairDTO> findMostProjectsOverlapPair(List<CSVEmployeeRowDTO> employeeRows) {
        Map<Integer, List<CSVEmployeeRowDTO>> empoyeesPerProjectMap = employeeRows.stream()
                .collect(Collectors.groupingBy(CSVEmployeeRowDTO::getProjectId));

        Map<EmployeePairDTO, Long> employeePairDays = new HashMap<>();
        Map<String, Long> employeePairTotalDays = new HashMap<>();

        for (List<CSVEmployeeRowDTO> employees : empoyeesPerProjectMap.values()) {

            for (int i = 0; i < employees.size(); i++) {
                for (int j = i + 1; j < employees.size(); j++) {
                    CSVEmployeeRowDTO firstEmployee = employees.get(i);
                    CSVEmployeeRowDTO secondEmployee = employees.get(j);

                    long overlapDays = calculateOverlapDays(firstEmployee, secondEmployee);

                    if (overlapDays == 0) {
                        continue;
                    }

                    EmployeePairDTO employeePair = EmployeePairDTO.builder()
                            .firstEmployeeId(firstEmployee.getEmployeeId())
                            .secondEmployeeId(secondEmployee.getEmployeeId())
                            .projectId(firstEmployee.getProjectId())
                            .daysWorked(overlapDays)
                            .build();

                    employeePairDays.put(employeePair, overlapDays);

                    String pairKey = createPairKey(firstEmployee.getEmployeeId(), secondEmployee.getEmployeeId());
                    employeePairTotalDays.merge(pairKey, overlapDays, Long::sum);
                }
            }
        }

        String maxPair = employeePairTotalDays.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        if (StringUtils.isEmpty(maxPair)) {
            return Collections.emptyList();
        }

        String[] employeeIds = maxPair.split("-");
        Integer firstEmployeeId = Integer.parseInt(employeeIds[0]);
        Integer secondEmployeeId = Integer.parseInt(employeeIds[1]);

        return employeePairDays.keySet().stream()
                .filter(pair -> isEmployeeIdPairMatch(pair, firstEmployeeId, secondEmployeeId))
                .sorted((pair1, pair2) -> Long.compare(pair2.getDaysWorked(), pair1.getDaysWorked()))
                .toList();
    }

    private boolean isEmployeeIdPairMatch(EmployeePairDTO employeePair, Integer firstEmployeeId, Integer secondEmployeeId) {
        Integer pairFirstId = employeePair.getFirstEmployeeId();
        Integer pairSecondId = employeePair.getSecondEmployeeId();

        return (pairFirstId.equals(firstEmployeeId) && pairSecondId.equals(secondEmployeeId)) ||
                (pairFirstId.equals(secondEmployeeId) && pairSecondId.equals(firstEmployeeId));
    }

    private String createPairKey(Integer firstEmployeeId, Integer secondEmployeeId) {
        return firstEmployeeId.compareTo(secondEmployeeId) <= 0
                ? firstEmployeeId + "-" + secondEmployeeId
                : secondEmployeeId + "-" + firstEmployeeId;
    }

    private long calculateOverlapDays(CSVEmployeeRowDTO firstEmployee, CSVEmployeeRowDTO secondEmployee) {
        LocalDate overlapStart = firstEmployee.getDateFrom().isAfter(secondEmployee.getDateFrom())
                ? firstEmployee.getDateFrom()
                : secondEmployee.getDateFrom();

        LocalDate overlapEnd = firstEmployee.getDateTo().isBefore(secondEmployee.getDateTo())
                ? firstEmployee.getDateTo()
                : secondEmployee.getDateTo();

        return overlapStart.isBefore(overlapEnd) || overlapStart.isEqual(overlapEnd)
                ? ChronoUnit.DAYS.between(overlapStart, overlapEnd)
                : 0;
    }
}
