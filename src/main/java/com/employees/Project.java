package com.employees;

import java.time.LocalDate;

public class Project {
    private String projectId;
    private int empId;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    public Project() {}
    public Project(String projectId, int empId, LocalDate dateFrom, LocalDate dateTo) {
        this.projectId = projectId;
        this.empId = empId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public String getProjectId() {
        return projectId;
    }
    public int getEmpId() {
        return empId;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }
}
