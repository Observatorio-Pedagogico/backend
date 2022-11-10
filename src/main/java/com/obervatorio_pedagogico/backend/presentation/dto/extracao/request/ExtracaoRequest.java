package com.obervatorio_pedagogico.backend.presentation.dto.extracao.request;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao.Status;
import com.obervatorio_pedagogico.backend.presentation.model.arquivo.Arquivo;

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
    
    @Valid
    @NotEmpty
    @NotBlank
    private String titulo;

    private String descricao;

    private Status status;

    private String emailRemetente;

    private LocalDateTime ultimaDataHoraAtualizacao;

    private List<MultipartFile> arquivosMultipartFile;

    private List<Arquivo> arquivos;
}