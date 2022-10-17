package com.obervatorio_pedagogico.backend.presentation.dto.extracao;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExtracaoRequest implements Serializable {

    private Long id;

    private String titulo;

    private String descricao;

    private Status status;

    private String periodoLetivo;

    private String emailRemetente;

    private LocalDateTime ultimaDataHoraAtualizacao;

    private List<MultipartFile> arquivosMultipartFile;

    private List<Arquivo> arquivos;
}