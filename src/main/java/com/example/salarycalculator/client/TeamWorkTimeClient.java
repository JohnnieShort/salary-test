package com.example.salarycalculator.client;

import com.example.salarycalculator.client.dto.TeamWorkTimeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@FeignClient(value = "teamWorkTime", url = "${client.team-work-time-url}")
public interface TeamWorkTimeClient {

    @GetMapping(value = "/period")
    List<TeamWorkTimeDto> getTeamWorkTime(@RequestParam Date from, @RequestParam Date to);
}
