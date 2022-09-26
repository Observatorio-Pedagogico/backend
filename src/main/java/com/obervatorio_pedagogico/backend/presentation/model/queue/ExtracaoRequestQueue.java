package com.obervatorio_pedagogico.backend.presentation.model.queue;

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
public class ExtracaoRequestQueue implements Serializable {

    private Long id;

    private String titulo;

    private String descricao;

    private Status status;

    private String periodoLetivo;

    private LocalDateTime ultimaDataHoraAtualizacao;

    private ArquivoQueue arquivoDisciplina;
    
    private ArquivoQueue arquivoAluno;
}