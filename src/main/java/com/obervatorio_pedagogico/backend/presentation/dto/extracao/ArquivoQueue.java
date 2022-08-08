package com.obervatorio_pedagogico.backend.presentation.dto.extracao;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArquivoQueue implements Serializable {

    private byte[] conteudoArquivo;

    private String tipo;
}
