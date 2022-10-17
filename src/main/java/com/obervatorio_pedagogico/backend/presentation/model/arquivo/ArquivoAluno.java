package com.obervatorio_pedagogico.backend.presentation.model.arquivo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.obervatorio_pedagogico.backend.presentation.model.arquivo.linhaArquivos.LinhaArquivoAluno;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArquivoAluno {

    private List<LinhaArquivoAluno> linhas = new ArrayList<>();

    public ArquivoAluno(Sheet sheet) {
        for (int i = 1; i < sheet.getLastRowNum(); i++) {
            Row linha = sheet.getRow(i);
            if (Objects.isNull(linha.getCell(0)) || linha.getCell(0).getStringCellValue().equals("")) continue;

            linhas.add(new LinhaArquivoAluno(linha));
        }
    }

    public static Boolean isConversivel(Sheet sheet) {
        LinhaArquivoAluno linha;

        try {
            linha = new LinhaArquivoAluno(sheet.getRow(1));
        } catch (Exception exception) {
            return false;
        }

        return Stream.of(linha.getMatricula(),
            linha.getCodigoDisciplina(),
            linha.getNomeDisciplina(),
            linha.getPeriodo(),
            linha.getNotas(),
            linha.getFrequencia())
            .allMatch(Objects::nonNull);
    }

    public Boolean proximoEMesmoAluno(Integer indexAtual) {
        LinhaArquivoAluno proximaLinha;
        try {
            proximaLinha = linhas.get(indexAtual + 1);
        } catch (IndexOutOfBoundsException exception) {
            return false;
        }
        return linhas.get(indexAtual).getMatricula().equals(proximaLinha.getMatricula());
    }
}
