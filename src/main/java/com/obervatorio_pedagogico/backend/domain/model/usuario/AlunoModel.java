package com.obervatorio_pedagogico.backend.domain.model.usuario;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_aluno")
public class AlunoModel extends UsuarioModel {

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "cre")
    private Float cre;

    @Column(name = "situacao_curso")
    private String situacaoCurso;

    @Column(name = "ano_ingresso")
    private Integer anoIngresso;

    @Column(name = "situacao_ultimo_periodo")
    private String situacaoUltimoPeriodo;

    @Column(name = "periodo_letivo")
    private Integer periodoLetivo;
}
