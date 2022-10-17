package com.obervatorio_pedagogico.backend.presentation.model.arquivo.linhaArquivos;

import org.apache.poi.ss.usermodel.Row;
import com.obervatorio_pedagogico.backend.infrastructure.utils.converters.NumberConverters;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinhaArquivoAluno {
    private String matricula;
    private String codigoDisciplina;
    private String nomeDisciplina;
    private String periodo;
    private String notas;
    private Integer frequencia;

    public LinhaArquivoAluno(Row row) {
        this.matricula = row.getCell(1).getStringCellValue();
        this.codigoDisciplina = row.getCell(2).getStringCellValue();
        this.nomeDisciplina = row.getCell(3).getStringCellValue();
        this.periodo = row.getCell(4).getStringCellValue();
        this.notas = row.getCell(5).getStringCellValue();
        this.frequencia = NumberConverters.stringToInteger(row.getCell(6).getStringCellValue());
    }

    public LinhaArquivoAluno() {}
}
