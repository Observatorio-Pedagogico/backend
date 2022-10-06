package com.obervatorio_pedagogico.backend.presentation.dto.extracao;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Arquivo implements Serializable {

    private final String XLS_CODE = "application/vnd.ms-excel";
    private final String XLSX_CODE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private MultipartFile conteudo;

    private String tipo;

    public boolean isXls() {
        return tipo.equals(XLS_CODE);
    }

    public boolean isXlsx() {
        return tipo.equals(XLSX_CODE);
    }

    public boolean isSuportado() {
        return (isXls() || isXlsx());
    }
}
