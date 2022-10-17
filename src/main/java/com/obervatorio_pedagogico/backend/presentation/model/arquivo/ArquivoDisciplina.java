package com.obervatorio_pedagogico.backend.presentation.model.arquivo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.obervatorio_pedagogico.backend.presentation.model.arquivo.linhaArquivos.LinhaArquivoDisciplina;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArquivoDisciplina {

    private List<LinhaArquivoDisciplina> linhas = new ArrayList<>();

    public ArquivoDisciplina(Sheet sheet) {
        for (int i = 1; i < sheet.getLastRowNum(); i++) {
            Row linha = sheet.getRow(i);
            if (Objects.isNull(linha.getCell(0))) continue;

            linhas.add(new LinhaArquivoDisciplina(linha));
        }
    }

    public static Boolean isConversivel(Sheet sheet) {
        LinhaArquivoDisciplina linha;
        try {
            linha = new LinhaArquivoDisciplina(sheet.getRow(1));

        } catch (Exception exception) {
            return false;
        }

        return Stream.of(linha.getMatricula(),
            linha.getNomeAluno(),
            linha.getCodigoDisciplina(),
            linha.getNomeDisciplina(),
            linha.getCargaHoraria(),
            linha.getPeriodoLetivo(),
            linha.getMedia(),
            linha.getFrequencia(),
            linha.getSituacao(),
            linha.getIdade(),
            linha.getSexo(),
            linha.getCre(),
            linha.getQuantidadeMatriculas(),
            linha.getQuantidadeReprovados())
            .allMatch(Objects::nonNull);
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
