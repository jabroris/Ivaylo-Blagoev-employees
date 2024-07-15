package com.employees;

import java.util.Map;

public class CommonProjectDetails {
    private int empId1;
    private int empId2;
    private Map<String, Integer> projectTime;

    public CommonProjectDetails(int empId1, int empId2, Map<String, Integer> projectTime) {
        this.empId1 = empId1;
        this.empId2 = empId2;
        this.projectTime = projectTime;
    }

    public int getEmpId1() {
        return empId1;
    }

    public int getEmpId2() {
        return empId2;
    }

    public Map<String, Integer> getProjectTime() {
        return projectTime;
    }
}
