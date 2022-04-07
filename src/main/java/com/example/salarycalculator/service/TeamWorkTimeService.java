package com.example.salarycalculator.service;

import com.example.salarycalculator.client.TeamWorkTimeClient;
import com.example.salarycalculator.client.dto.TeamWorkTimeDto;
import com.example.salarycalculator.model.Employee;
import com.example.salarycalculator.model.EmployeeWithSalary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamWorkTimeService {

    private final TeamWorkTimeClient teamWorkTimeClient;

    public List<EmployeeWithSalary> setSalaryForTeam(List<Employee> team, Double moneyAmount, Date from, Date to) {
        if (team.isEmpty() || moneyAmount <= 0 || from == null || to == null) {
            throw new IllegalArgumentException("Illegal values in passed args.");
        }
        final List<Integer> mitarbeiterIds = team.stream().map(Employee::getMitarbeiterId).distinct().collect(Collectors.toList());

        if (mitarbeiterIds.size() > 1) {
            throw new IllegalArgumentException("Illegal member in team");
        }

        List<TeamWorkTimeDto> teamWorkTime = teamWorkTimeClient.getTeamWorkTime(from, to);
        Integer workedTimeAmountIncoming = teamWorkTime
                .stream()
                .filter(teamWorkTimeDto -> teamWorkTimeDto.getMitarbeiterId().equals(mitarbeiterIds.get(0)))
                .map(TeamWorkTimeDto::getDauer)
                .reduce(0, Integer::sum);

        Integer workedTimeAmountTeam = team.stream().map(Employee::getWorkTimeAmountTimeUnits).reduce(0, Integer::sum);

        if (!workedTimeAmountTeam.equals(workedTimeAmountIncoming)) {
            throw new IllegalArgumentException("Incorrect time amount received");
        }

        final double moneyPerTimeUnit = moneyAmount / workedTimeAmountIncoming;

        return team.stream().map(member -> mapToEmployeeWithSalary(member, moneyPerTimeUnit)).collect(Collectors.toList());
    }

    private EmployeeWithSalary mapToEmployeeWithSalary(Employee member, double moneyPerTimeUnit) {
        final EmployeeWithSalary result = new EmployeeWithSalary();
        result.setMitarbeiterId(member.getMitarbeiterId());
        result.setWorkTimeAmountTimeUnits(member.getWorkTimeAmountTimeUnits());
        result.setSalary(member.getWorkTimeAmountTimeUnits() * moneyPerTimeUnit);
        return result;
    }
}
