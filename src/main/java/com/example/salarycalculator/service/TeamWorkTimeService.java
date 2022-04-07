package com.example.salarycalculator.service;

import com.example.salarycalculator.client.TeamWorkTimeClient;
import com.example.salarycalculator.client.dto.TeamWorkTimeDto;
import com.example.salarycalculator.model.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamWorkTimeService {

    private final TeamWorkTimeClient teamWorkTimeClient;

    public void setSalaryForTeam(List<Employee> team, Double moneyAmount, Date from, Date to) {
        if (team.isEmpty() || moneyAmount <= 0 || from == null || to == null) {
            throw new IllegalArgumentException("Illegal values in passed args.");
        }

        final Integer mitarbeiterId = team.get(0).getMitarbeiterId();
        List<TeamWorkTimeDto> teamWorkTime = teamWorkTimeClient.getTeamWorkTime(from, to);
        Integer workedTimeAmount = teamWorkTime
                .stream()
                .filter(teamWorkTimeDto -> teamWorkTimeDto.getMitarbeiterId().equals(mitarbeiterId))
                .map(TeamWorkTimeDto::getDauer)
                .reduce(0, Integer::sum);

        final double moneyPerMinute = moneyAmount / workedTimeAmount;

        team.forEach(member -> member.setSalary(member.getSalary() + member.getWorkTimeAmountMinutes() * moneyPerMinute));
    }
}
