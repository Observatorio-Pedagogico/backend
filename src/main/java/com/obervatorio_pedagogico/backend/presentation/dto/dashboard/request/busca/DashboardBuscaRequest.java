package com.obervatorio_pedagogico.backend.presentation.dto.dashboard.request.busca;

import java.util.List;

import com.obervatorio_pedagogico.backend.domain.model.disciplina.Disciplina;
import com.obervatorio_pedagogico.backend.infrastructure.utils.buscaConstrutor.PredicatesGenerator.Operation;
import com.obervatorio_pedagogico.backend.infrastructure.utils.buscaConstrutor.PredicatesGenerator.SearchEntity;
import com.obervatorio_pedagogico.backend.infrastructure.utils.buscaConstrutor.PredicatesGenerator.SearchParam;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SearchEntity(Disciplina.class)
public class DashboardBuscaRequest {

    @SearchParam(property = "id",  operation = Operation.EQUALS)
    private List<Long> id;

    @SearchParam(property = "periodoLetivo",  operation = Operation.EQUALS)
    private List<String> periodoLetivo;

    @SearchParam(property = "codigo",  operation = Operation.EQUALS)
    private List<String> codigo;

    private Boolean ignorarAusencia = false;
}