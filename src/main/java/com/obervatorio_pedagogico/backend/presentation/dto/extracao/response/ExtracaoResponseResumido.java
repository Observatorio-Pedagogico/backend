package com.obervatorio_pedagogico.backend.presentation.dto.extracao.response;

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
public class ExtracaoResponseResumido {

    private Long id;

    private String titulo;

    private Status status;

    private LocalDateTime dataCadastro;

    private LocalDateTime ultimaDataHoraAtualizacao;
}
