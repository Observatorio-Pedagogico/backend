package com.obervatorio_pedagogico.backend.presentation.model.queue;

import java.io.Serializable;

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
