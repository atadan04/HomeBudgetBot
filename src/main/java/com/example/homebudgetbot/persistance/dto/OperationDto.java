package com.example.homebudgetbot.persistance.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;
@Data
@Builder
public class OperationDto {
    private UUID id;
    private double sum;
    private UUID categoryOperationId;
    private Integer typeOperationId;

}
