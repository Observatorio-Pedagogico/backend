package com.obervatorio_pedagogico.backend.domain.model.disciplina;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.obervatorio_pedagogico.backend.domain.model.FrequenciaSituacao.FrequenciaSituacao;
import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Aluno;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_disciplina")
public class Disciplina implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "nome")
    private String nome;

    @Column(name = "periodo_matriz")
    private String periodoMatriz;

    @Column(name = "periodo_letivo")
    private String periodoLetivo;
    
    @ManyToMany(fetch = FetchType.LAZY,
    cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH
    })
    @JoinTable(
            name = "t_disciplina_alunos",
            joinColumns = @JoinColumn (name = "id_disciplina"),
            inverseJoinColumns = @JoinColumn(name = "id_aluno")
    )
    private List<Aluno> alunos = new ArrayList<>();

    @OneToMany(
        mappedBy = "disciplina", 
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Nota> notas = new ArrayList<>();

    @OneToMany(
        mappedBy = "disciplina", 
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<FrequenciaSituacao> frequenciaSituacoes = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY,
    mappedBy = "disciplinas")
    private List<Extracao> extracoes = new ArrayList<>();

    public boolean addExtracao(Extracao extracao) {
        if(!hasExtracao(extracao)) {
            return extracoes.add(extracao);
        }
        return false;
    }

    public boolean removeExtracao(Extracao extracao) {
        Integer tamanhoExtracoes = this.extracoes.size();
        this.extracoes = extracoes.stream().filter(ext -> !ext.getId().equals(extracao.getId())).collect(Collectors.toList());
        return tamanhoExtracoes > this.extracoes.size();
    }

    public Boolean addAluno(Aluno aluno) {
        if (!hasAlunos(aluno))
            return alunos.add(aluno);
        return false;
    }

    public boolean removeAluno(Aluno aluno) {
        Integer tamanhoAlunos = this.alunos.size();
        this.alunos = alunos.stream().filter(alu -> !alu.getId().equals(aluno.getId())).collect(Collectors.toList());
        return tamanhoAlunos > this.alunos.size();
    }

    public boolean hasAlunos(Aluno aluno) {
        return alunos.stream()
            .anyMatch(alunoFiltro -> alunoFiltro.getMatricula().equals(aluno.getMatricula()));
    }

    public boolean hasAlunosById(Aluno aluno) {
        return alunos.stream()
        .anyMatch(alunoFiltro -> alunoFiltro.getId().equals(aluno.getId()));
    }

    public boolean hasExtracao(Extracao extracao) {
        return extracoes.stream().anyMatch(extrcaoFiltro -> (extrcaoFiltro.getTitulo().equals(extracao.getTitulo())) 
            && extrcaoFiltro.getPeriodoLetivo().equals(extracao.getPeriodoLetivo()));
    }

    public boolean isPassivoDeletar() {
        return this.getExtracoes().isEmpty();
    }
}