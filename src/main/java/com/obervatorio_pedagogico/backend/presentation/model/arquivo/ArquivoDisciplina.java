package com.obervatorio_pedagogico.backend.presentation.model.arquivo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.obervatorio_pedagogico.backend.presentation.model.arquivo.linhaArquivos.LinhaArquivoDisciplina;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArquivoDisciplina {

    private List<LinhaArquivoDisciplina> linhas = new ArrayList<>();

    public ArquivoDisciplina(Sheet sheet) {
        for (int i = 1; i < sheet.getLastRowNum(); i++) {
            Row linha = sheet.getRow(i);
            if (Objects.isNull(linha.getCell(0))) continue;

            linhas.add(new LinhaArquivoDisciplina(linha));
        }
    }

    public Boolean proximoEMesmoAluno(Integer indexAtual) {
        LinhaArquivoDisciplina proximaLinha;
        try {
            proximaLinha = linhas.get(indexAtual + 1);
        } catch (IndexOutOfBoundsException exception) {
            return false;
        }
        return linhas.get(indexAtual).getMatricula().equals(proximaLinha.getMatricula());
    }
}
