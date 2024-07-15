package com.employees;

import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ProjectService {

    public CommonProjectDetails loadEmployeesFromCsv(String filePath) {
        Map<String, List<Project>> projects = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int empId = Integer.parseInt(values[0]);
                String projectId = values[1];
                LocalDate dateFrom = parseDate(values[2]);
                LocalDate dateTo = values[3].equals("NULL") ? LocalDate.now() : parseDate(values[3]);
                projects.computeIfAbsent(projectId, k -> new ArrayList<>()).add(new Project(projectId, empId, dateFrom, dateTo));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return calculateMaxContribution(projects);
    }

    private LocalDate parseDate(String dateStr) {
        List<DateTimeFormatter> formatters = Arrays.asList(
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("MM/dd/yyyy")
        );
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(dateStr, formatter);
            } catch (Exception e) {
                // TODO: add another parser
            }
        }
        throw new IllegalArgumentException("Invalid date format: " + dateStr);
    }

    private CommonProjectDetails calculateMaxContribution(Map<String, List<Project>> empProjects) {
        int empId1 = -1;
        int empId2 = -1;

        Map<Set<Integer>, Map<String, Integer>> employeePairs = getEmployeePairs(empProjects);

        Map.Entry<Set<Integer>, Map<String, Integer>> longestPair = employeePairs.entrySet().stream()
                .max(Map.Entry.comparingByValue(Comparator.comparingInt(
                        durations -> durations.values().stream()
                                .mapToInt(Integer::intValue)
                                .sum())))
                .orElseThrow(() -> new RuntimeException("No pairs found"));

        Set<Integer> empIds = longestPair.getKey();
        Iterator<Integer> iterator = empIds.iterator();
        empId1 = iterator.next();
        empId2 = iterator.next();

        Map<String, Integer> projectDurations = longestPair.getValue();

        return new CommonProjectDetails(empId1, empId2, projectDurations);
    }

    private Map<Set<Integer>, Map<String, Integer>> getEmployeePairs(Map<String, List<Project>> empProjects) {
        Map<Set<Integer>, Map<String, Integer>> employeePairs = new HashMap<>();

        for (List<Project> projects : empProjects.values()) {
            for (int i = 0; i < projects.size(); i++) {
                for (int j = i + 1; j < projects.size(); j++) {
                    Project p1 = projects.get(i);
                    Project p2 = projects.get(j);

                    if (p1.getEmpId() != p2.getEmpId()) {
                        Set<Integer> pair = new HashSet<>(Arrays.asList(p1.getEmpId(), p2.getEmpId()));
                        LocalDate start = p1.getDateFrom().isAfter(p2.getDateFrom()) ? p1.getDateFrom() : p2.getDateFrom();
                        LocalDate end = p1.getDateTo().isBefore(p2.getDateTo()) ? p1.getDateTo() : p2.getDateTo();
                        long days = ChronoUnit.DAYS.between(start, end);
                        String currentProjectId = p1.getProjectId();

                        employeePairs.computeIfAbsent(pair, k -> new HashMap<>())
                                .put(currentProjectId, employeePairs.get(pair).getOrDefault(currentProjectId, 0) + (int) days);
                    }
                }
            }
        }
        return employeePairs;
    }
}


