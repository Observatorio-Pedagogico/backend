package com.obervatorio_pedagogico.backend.presentation.dto.dashboard.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DashboardResponse {
    private List<String> legendas;
    
    private List<ConjuntoDadosResponse> conjuntoDados;
}
