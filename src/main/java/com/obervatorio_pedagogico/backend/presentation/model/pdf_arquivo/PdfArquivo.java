package com.obervatorio_pedagogico.backend.presentation.model.pdf_arquivo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PdfArquivo {
    private List<PdfArquivoSubParte> subPartes;
}
