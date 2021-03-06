package com.obervatorio_pedagogico.backend.presentation.dto.extracao;

import java.io.Serializable;
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
public class ExtracaoResponse implements Serializable {

    private String titulo;

    private String descricao;

    private Status status;

    private Integer anoLetivo;

    private LocalDateTime ultimaDataHoraAtualizacao;
}