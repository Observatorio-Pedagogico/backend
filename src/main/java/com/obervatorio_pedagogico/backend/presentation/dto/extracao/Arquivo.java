package com.obervatorio_pedagogico.backend.presentation.dto.extracao;

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

    private MultipartFile conteudo;

    public Boolean isXls() {
        if (conteudo.getContentType().equals(XLS_CODE))
            return true;
        return false;
    }
}
