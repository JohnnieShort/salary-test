package com.example.salarycalculator.service;

import com.example.salarycalculator.client.TeamWorkTimeClient;
import com.example.salarycalculator.client.dto.TeamWorkTimeDto;
import com.example.salarycalculator.model.Employee;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.randomizers.range.IntegerRangeRandomizer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class TeamWorkTimeServiceTest {

    @Autowired
    private TeamWorkTimeService subject;

    EasyRandomParameters parameters = new EasyRandomParameters().randomize(Integer.class, new IntegerRangeRandomizer(1, 1000));

    private final EasyRandom random = new EasyRandom(parameters);

    @MockBean
    private TeamWorkTimeClient teamWorkTimeClient;

    @Test
    void setSalaryForTeam_ShouldCorrectlyDistributeMoneyAmount() {
        final String teamIdStr = String.valueOf(Math.random());
        final Integer mitArbeiterId = Integer.valueOf(teamIdStr.substring(teamIdStr.length() -3));
        final List<Employee> teamMock = getTeamMock(mitArbeiterId);
        final Integer timeWorkedByTeam = teamMock
                .stream()
                .map(Employee::getWorkTimeAmountMinutes)
                .reduce(0, Integer::sum);
        final List<TeamWorkTimeDto> teamWorkTimeMock = getTeamWorkTimeMock(mitArbeiterId, timeWorkedByTeam);
        Mockito.when(teamWorkTimeClient.getTeamWorkTime(any(Date.class), any(Date.class))).thenReturn(teamWorkTimeMock);

        subject.setSalaryForTeam(teamMock, 10000.0, new Date(), new Date());

        assertEquals(teamWorkTimeMock.get(0).getDauer(), teamMock.stream().map(Employee::getWorkTimeAmountMinutes).reduce(0, Integer::sum));
        assertEquals(10000, teamMock.stream().map(Employee::getSalary).reduce(0.0, Double::sum));
    }

    private List<Employee> getTeamMock(Integer mitArbeiterId) {
        List<Employee> result = new ArrayList<>();
        IntStream.range(0, 5).forEach(i -> result.add(getEmployeeMock(mitArbeiterId)));
        return result;
    }

    private Employee getEmployeeMock(Integer mitArbeiterId) {
        Employee employee = random.nextObject(Employee.class);
        employee.setMitarbeiterId(mitArbeiterId);
        employee.setSalary(0.0);
        return employee;
    }

    private List<TeamWorkTimeDto> getTeamWorkTimeMock(Integer mitArbeiterId, Integer timeWorkedByTeam) {
        List<TeamWorkTimeDto> result = new ArrayList<>();
        IntStream.range(0, 3).forEach(i -> result.add(getTeamWorkTimeUnitMOck()));
        result.get(0).setMitarbeiterId(mitArbeiterId);
        result.get(0).setDauer(timeWorkedByTeam);
        return result;
    }

    private TeamWorkTimeDto getTeamWorkTimeUnitMOck() {
        return random.nextObject(TeamWorkTimeDto.class);
    }
}