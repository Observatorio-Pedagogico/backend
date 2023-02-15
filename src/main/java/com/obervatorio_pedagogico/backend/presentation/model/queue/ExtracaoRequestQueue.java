package com.obervatorio_pedagogico.backend.presentation.model.queue;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao.Status;
import com.obervatorio_pedagogico.backend.presentation.model.usuario.EnvelopeFuncionario.TipoFuncionario;

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

    private FuncionarioCopedQueue funcionarioCopedRemetente;

    private ProfessorQueue professorRemetente;

    private TipoFuncionario tipoFuncionario;

    private LocalDateTime ultimaDataHoraAtualizacao;

    private List<ArquivoQueue> arquivoQueues;
}