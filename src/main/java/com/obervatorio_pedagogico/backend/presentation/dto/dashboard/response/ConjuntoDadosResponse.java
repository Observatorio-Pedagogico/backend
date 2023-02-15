package com.obervatorio_pedagogico.backend.presentation.dto.dashboard.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConjuntoDadosResponse {
    private String legenda;
    
    private List<BigDecimal> dados;
}
