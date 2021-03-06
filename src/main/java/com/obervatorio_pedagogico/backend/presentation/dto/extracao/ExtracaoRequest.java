package com.obervatorio_pedagogico.backend.presentation.dto.extracao;

import java.time.LocalDateTime;

import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExtracaoRequest {

    private String titulo;

    private String descricao;

    private Status status;

    private Integer anoLetivo;

    private LocalDateTime ultimaDataHoraAtualizacao;

    private Arquivo arquivo;
}