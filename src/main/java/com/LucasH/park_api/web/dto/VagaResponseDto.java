package com.LucasH.park_api.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.service.annotation.GetExchange;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VagaResponseDto {
    private Long id;
    private String codigo;
    private String status;
}
