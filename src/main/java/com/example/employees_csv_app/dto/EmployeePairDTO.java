package com.example.employees_csv_app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class EmployeePairDTO {
    private Integer firstEmployeeId;
    private Integer secondEmployeeId;
    private Integer projectId;
    private long daysWorked;
}
