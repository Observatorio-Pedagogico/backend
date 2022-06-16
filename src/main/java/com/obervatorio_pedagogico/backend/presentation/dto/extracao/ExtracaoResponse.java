package com.obervatorio_pedagogico.backend.presentation.dto.extracao;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao.Status;
import com.obervatorio_pedagogico.backend.presentation.dto.disciplina.DisciplinaResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExtracaoResponse implements Serializable {

    private Long id;

    private Integer porcentagemDeEnvio = 0;

    private Integer totalLinhas = 0;

    private String titulo;

    private String descricao;

    private Status status;

    private String periodoLetivo;
    
    private LocalDateTime ultimaDataHoraAtualizacao;

    private List<DisciplinaResponse> disciplinas;
}