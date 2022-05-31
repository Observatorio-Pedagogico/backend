package com.obervatorio_pedagogico.backend.domain.model.usuario;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
    
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Nota> notas;
    
    @ManyToMany(mappedBy = "alunos", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Disciplina> disciplinas;

    public Boolean addDisciplina(Disciplina disciplina) {
        if (Objects.isNull(disciplinas))
            disciplinas = new ArrayList<>();
        if (!hasDisciplina(disciplina))
            return disciplinas.add(disciplina);
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

    public Boolean removeNota(Nota nota) {
        if (Objects.isNull(notas))
            notas = new ArrayList<>();
        return notas.remove(nota);
    }

    public Boolean hasDisciplina(Disciplina disciplina) {
        return disciplinas.stream()
            .filter(disciplinaFiltro -> disciplinaFiltro.getId().equals(disciplina.getId()) 
                || (disciplinaFiltro.getNome().equals(disciplina.getNome())
                    && disciplinaFiltro.getPeriodoLetivo().equals(disciplina.getPeriodoLetivo())
                    && disciplinaFiltro.getProfessor().equals("ahhhhhhhh"))
            ).findFirst()
            .isPresent();
    }
    
    public Boolean hasNota(Nota nota) {
        return notas.stream()
            .filter(notaFiltro -> notaFiltro.getId().equals(nota.getId()) 
                || (notaFiltro.getValor().equals(nota.getValor())
                    && notaFiltro.getOrdem().equals(nota.getOrdem())
                    && notaFiltro.getTipo().equals(nota.getTipo())
                    && notaFiltro.getDisciplina().equals(nota.getDisciplina())
                    && notaFiltro.getDisciplina().getPeriodoLetivo().equals(nota.getDisciplina().getPeriodoLetivo()))
            ).findFirst()
            .isPresent();
    }
}
