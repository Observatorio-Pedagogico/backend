package com.obervatorio_pedagogico.backend.presentation.dto.extracao;

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
public class Arquivo {

    private final String XLS_CODE = "application/vnd.ms-excel";
    private final String XLSX_CODE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private MultipartFile conteudo;

    private String tipo;

    private void initTipo() {
        if(Objects.isNull(tipo))
            tipo = conteudo.getContentType();
    }

    public Boolean isXls() {
        initTipo();
        if (tipo.equals(XLS_CODE))
            return true;
        return false;
    }

    public Boolean isXlsx() {
        initTipo();
        if (tipo.equals(XLSX_CODE))
            return true;
        return false;
    }

    public Boolean isSuportado() {
        if (isXls() || isXlsx())
            return true;
        return false;
    }
}
