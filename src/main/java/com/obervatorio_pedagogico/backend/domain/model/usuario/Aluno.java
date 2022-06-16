package com.obervatorio_pedagogico.backend.domain.model.usuario;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.obervatorio_pedagogico.backend.domain.model.FrequenciaSituacao.FrequenciaSituacao;
import com.obervatorio_pedagogico.backend.domain.model.disciplina.Disciplina;
import com.obervatorio_pedagogico.backend.domain.model.disciplina.Nota;

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

    private static final long serialVersionUID = 1L;

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
    
    @OneToMany(
        mappedBy = "aluno", 
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Nota> notas = new ArrayList<>();
    
    @ManyToMany(fetch = FetchType.LAZY,
    cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    },
    mappedBy = "alunos")
    private List<Disciplina> disciplinas = new ArrayList<>();

    @OneToMany(
        mappedBy = "aluno", 
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<FrequenciaSituacao> frequenciaSituacoes = new ArrayList<>();

    public Boolean addDisciplina(Disciplina disciplina) {
        if (Objects.isNull(disciplinas))
            disciplinas = new ArrayList<>();
        if (!hasDisciplina(disciplina)) {
            disciplina.addAluno(this);
            return disciplinas.add(disciplina);
        }
        return false;
    }

    public Boolean removeDisciplina(Disciplina disciplina) {
        if (Objects.isNull(disciplinas))
            disciplinas = new ArrayList<>();
        return disciplinas.remove(disciplina);
    }

    public Boolean addNota(Nota nota) {
        if (Objects.isNull(notas))
            notas = new ArrayList<>();
        if (!hasNota(nota))
            return notas.add(nota);
        return false;
    }

    public boolean removeNota(Nota nota) {
        if (Objects.isNull(notas))
            notas = new ArrayList<>();
        return notas.remove(nota);
    }

    public boolean hasDisciplina(Disciplina disciplina) {
        return disciplinas.stream()
            .anyMatch(disciplinaFiltro -> disciplinaFiltro.getNome().equals(disciplina.getNome())
            && disciplinaFiltro.getPeriodoLetivo().equals(disciplina.getPeriodoLetivo()));
    } 
    
    public boolean hasNota(Nota nota) {
        return notas.stream()
            .anyMatch(notaFiltro -> notaFiltro.getId().equals(nota.getId()) 
            || (notaFiltro.getValor().equals(nota.getValor())
                && notaFiltro.getOrdem().equals(nota.getOrdem())
                && notaFiltro.getTipo().equals(nota.getTipo())
                && notaFiltro.getDisciplina().equals(nota.getDisciplina())
                && notaFiltro.getDisciplina().getPeriodoLetivo().equals(nota.getDisciplina().getPeriodoLetivo())));
    }
}
