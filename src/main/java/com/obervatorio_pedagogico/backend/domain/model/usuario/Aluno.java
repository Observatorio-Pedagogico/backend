package com.obervatorio_pedagogico.backend.domain.model.usuario;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao;

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
public class Aluno extends Usuario implements Serializable {

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
    
    @ManyToMany(mappedBy = "alunos", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Extracao> extracoes;

    public Boolean addExtracao(Extracao extracao) {
        if (Objects.isNull(extracoes))
            extracoes = new HashSet<>();
        return extracoes.add(extracao);
    }

    public Boolean removeExtracao(Extracao extracao) {
        if (Objects.isNull(extracoes))
            extracoes = new HashSet<>();
        return extracoes.remove(extracao);
    }
}
