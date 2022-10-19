package com.obervatorio_pedagogico.backend.presentation.dto.disciplina.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DisciplinaResumidoResponse {
    private Long id;

    private String codigo;

    private String nome;

    private String periodoMatriz;

    private String periodoLetivo;
}
