package com.example.salarycalculator.client.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TeamWorkTimeDto {
    private Integer arbeitszeitId;
    private Integer mitarbeiterId;
    private Date datum;
    private Date beginn;
    private Date ende;
    private Integer dauer;
}
