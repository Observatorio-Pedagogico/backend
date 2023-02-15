package com.obervatorio_pedagogico.backend.presentation.model.pdf_arquivo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PdfArquivoSubParte {
    private String conteudo;
    private String tituloConteudo;
    private PdfArquivoSubParteTipo tipo;

    public enum PdfArquivoSubParteTipo {
        TITULO, IMAGEM, TEXTO;
    }
}
