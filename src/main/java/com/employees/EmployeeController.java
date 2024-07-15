package com.employees;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class EmployeeController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/longest-working-pair")
    public CommonProjectDetails getProjects(@RequestParam String filePath) {
        return projectService.loadEmployeesFromCsv(filePath);
    }
}
