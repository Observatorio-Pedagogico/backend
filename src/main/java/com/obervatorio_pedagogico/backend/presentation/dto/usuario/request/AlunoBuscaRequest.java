package com.obervatorio_pedagogico.backend.presentation.dto.usuario.request;

import java.util.List;

import com.obervatorio_pedagogico.backend.domain.model.usuario.Aluno;
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
@SearchEntity(Aluno.class)
public class AlunoBuscaRequest {

    @SearchParam(property = "periodoLetivo",  operation = Operation.EQUALS)
    private List<String> periodoLetivo;

    @SearchParam(property = "disciplinas.codigo",  operation = Operation.EQUALS)
    private List<String> codigo;
}
