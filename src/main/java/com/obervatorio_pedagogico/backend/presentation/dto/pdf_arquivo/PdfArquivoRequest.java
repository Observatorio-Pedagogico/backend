package com.obervatorio_pedagogico.backend.presentation.dto.pdf_arquivo;

import java.util.List;

import com.obervatorio_pedagogico.backend.presentation.model.pdf_arquivo.PdfArquivoSubParte;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PdfArquivoRequest {
    private List<PdfArquivoSubParte> subPartes;
}
