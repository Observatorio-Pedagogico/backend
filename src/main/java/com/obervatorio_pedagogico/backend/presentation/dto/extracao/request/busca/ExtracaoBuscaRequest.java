package com.obervatorio_pedagogico.backend.presentation.dto.extracao.request.busca;

import java.util.List;

import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao;
import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao.Status;
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
@SearchEntity(Extracao.class)
public class ExtracaoBuscaRequest {
    @SearchParam(property = "id",  operation = Operation.EQUALS)
    private List<Long> id;

    @SearchParam(property = "titulo",  operation = Operation.EQUALS)
    private List<String> titulo;
    
    @SearchParam(property = "descricao",  operation = Operation.ILIKE)
    private List<String> descricao;
    
    @SearchParam(property = "status",  operation = Operation.EQUALS)
    private List<Status> status;

    @SearchParam(property = "professorRemetente.id",  operation = Operation.EQUALS)
    private Long idProfessor;
}
