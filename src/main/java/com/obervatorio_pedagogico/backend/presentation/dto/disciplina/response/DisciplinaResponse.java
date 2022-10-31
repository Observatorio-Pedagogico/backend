package com.obervatorio_pedagogico.backend.presentation.dto.disciplina.response;

import java.util.List;

import com.obervatorio_pedagogico.backend.presentation.dto.usuario.response.AlunoResumidoResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DisciplinaResponse {
    private Long id;

    private String codigo;

    private Integer cargaHoraria;

    private String nome;

    private String periodoMatriz;

    private String periodoLetivo;

    private List<AlunoResumidoResponse> alunos;
}
