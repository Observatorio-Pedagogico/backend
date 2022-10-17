package com.obervatorio_pedagogico.backend.domain.model.usuario;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.obervatorio_pedagogico.backend.domain.model.FrequenciaSituacao.FrequenciaSituacao;
import com.obervatorio_pedagogico.backend.domain.model.disciplina.Disciplina;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_aluno")
public class Aluno extends Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "cre")
    private BigDecimal cre;

    @Column(name = "situacao_curso")
    private String situacaoCurso;

    @Column(name = "ano_ingresso")
    private Integer anoIngresso;
    
    @ManyToMany(fetch = FetchType.LAZY,
    mappedBy = "alunos")
    private List<Disciplina> disciplinas = new ArrayList<>();

    @OneToMany(
        mappedBy = "aluno", 
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<FrequenciaSituacao> frequenciaSituacoes = new ArrayList<>();

    public boolean addDisciplina(Disciplina disciplina) {
        if (!hasDisciplina(disciplina)) {
            return disciplinas.add(disciplina);
        }
        return false;
    }

    public boolean removeDisciplina(Disciplina disciplina) {
        Integer tamanhoDisciplinas = this.disciplinas.size();
        this.disciplinas = this.disciplinas.stream().filter(disc -> !disc.getId().equals(disciplina.getId())).collect(Collectors.toList());
        return tamanhoDisciplinas > this.disciplinas.size();
    }

    public boolean addFrequenciaSituacoes(FrequenciaSituacao frequenciaSituacao) {
        return frequenciaSituacoes.add(frequenciaSituacao);
    }

    public boolean removeFrequenciaSituacoes(FrequenciaSituacao frequenciaSituacao) {
        Integer tamanhoFrequenciaSituacoes = this.frequenciaSituacoes.size();
        this.frequenciaSituacoes = this.frequenciaSituacoes.stream().filter(freq -> !freq.getId().equals(frequenciaSituacao.getId())).collect(Collectors.toList());
        return tamanhoFrequenciaSituacoes > this.frequenciaSituacoes.size();
    }

    public boolean hasDisciplina(Disciplina disciplina) {
        return disciplinas.stream()
            .anyMatch(disciplinaFiltro -> disciplinaFiltro.getCodigo().equals(disciplina.getCodigo())
            && disciplinaFiltro.getPeriodoLetivo().equals(disciplina.getPeriodoLetivo()));
    }
    
    public boolean isPassivoDeletar() {
        return this.disciplinas.isEmpty();
    }
}
