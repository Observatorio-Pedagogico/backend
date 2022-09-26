package com.obervatorio_pedagogico.backend.presentation.model.arquivo.linhaArquivos;

import org.apache.poi.ss.usermodel.Row;

import com.obervatorio_pedagogico.backend.domain.model.FrequenciaSituacao.FrequenciaSituacao.SituacaoDisciplina;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Usuario.Sexo;
import com.obervatorio_pedagogico.backend.infrastructure.utils.converters.NumberConverters;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinhaArquivoDisciplina {
    private String matricula;
    private String nomeAluno;
    private String codigoDisciplina;
    private String nomeDisciplina;
    private Integer cargaHoraria;
    private Integer anoLetivo;
    private Integer periodoLetivo;
    private Float media;
    private Integer frequencia;
    private SituacaoDisciplina situacao;
    private Integer idade;
    private Sexo sexo;
    private Float cre;
    private Integer quantidadeMatriculas;
    private Integer quantidadeReprovados;

    public LinhaArquivoDisciplina(Row row) {
        this.matricula = row.getCell(1).getStringCellValue();
        this.nomeAluno = row.getCell(2).getStringCellValue();
        this.codigoDisciplina = row.getCell(4).getStringCellValue();
        this.nomeDisciplina = row.getCell(5).getStringCellValue();
        this.cargaHoraria = NumberConverters.stringToInteger(row.getCell(6).getStringCellValue());
        this.anoLetivo = NumberConverters.stringToInteger(row.getCell(7).getStringCellValue());
        this.periodoLetivo = NumberConverters.stringToInteger(row.getCell(8).getStringCellValue());
        this.media = NumberConverters.stringToFloat(row.getCell(9).getStringCellValue());
        this.frequencia = NumberConverters.stringToInteger(row.getCell(10).getStringCellValue());
        this.situacao = converterSituacaoDisciplina(row.getCell(11).getStringCellValue());
        this.idade = NumberConverters.stringToInteger(row.getCell(12).getStringCellValue());
        this.sexo = converterSexo(row.getCell(13).getStringCellValue());
        this.cre = NumberConverters.stringToFloat(row.getCell(14).getStringCellValue());
        this.quantidadeMatriculas = NumberConverters.stringToInteger( row.getCell(19).getStringCellValue());
        this.quantidadeReprovados = NumberConverters.stringToInteger(row.getCell(18).getStringCellValue());
    }

    public LinhaArquivoDisciplina() {}

    public Sexo converterSexo(String sexoInput) {
        switch(sexoInput) {
            case "M":
                return Sexo.MASCULINO;
            case "F":
                return Sexo.FEMININO;
            default:
                return null;
        }
    }

    public SituacaoDisciplina converterSituacaoDisciplina(String situacao) {
        situacao = situacao.replaceAll(" ", "_").toUpperCase();

        return SituacaoDisciplina.valueOf(situacao);
    }
}
