package com.obervatorio_pedagogico.backend.domain.model.disciplina;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import com.obervatorio_pedagogico.backend.domain.model.FrequenciaSituacao.FrequenciaSituacao.SituacaoDisciplina;
import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Aluno;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Usuario.Sexo;

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

    @Column(name = "cargaHoraria")
    private Integer cargaHoraria;

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

    public boolean addFrequenciaSituacoes(FrequenciaSituacao frequenciaSituacao) {
        return frequenciaSituacoes.add(frequenciaSituacao);
    }

    public boolean removeFrequenciaSituacoes(FrequenciaSituacao frequenciaSituacao) {
        Integer tamanhoFrequenciaSituacoes = this.frequenciaSituacoes.size();
        this.frequenciaSituacoes = this.frequenciaSituacoes.stream().filter(freq -> !freq.getId().equals(frequenciaSituacao.getId())).collect(Collectors.toList());
        return tamanhoFrequenciaSituacoes > this.frequenciaSituacoes.size();
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
            && extrcaoFiltro.getDescricao().equals(extracao.getDescricao()));
    }

    public boolean isPassivoDeletar() {
        return this.getExtracoes().isEmpty();
    }

    public SituacaoDisciplina obterFrequenciaSituacaoPorIdAluno(Long id) {
        Optional<FrequenciaSituacao> frequenciaOpcional = getFrequenciaSituacoes().stream().filter(frequenciaSituacao -> frequenciaSituacao.getAluno().getId().equals(id)).findFirst();
        
        if (!frequenciaOpcional.isPresent()) return null;

        return frequenciaOpcional.get().getSituacaoDisciplina();
    }

    public Integer getQuantidadeAlunosPorSexo(Sexo sexo, Boolean ignorarAusencia) {
        return (int) getAlunos().stream().filter(aluno -> {
            return (
                !ignorarAusencia ||
                (ignorarAusencia && !obterFrequenciaSituacaoPorIdAluno(aluno.getId()).isAusencia())
            ) &&
            aluno.getSexo().equals(sexo); 
        }).count();
    }

    public Integer getQuantidadeAlunosPorSexo(Sexo sexo) {
        return getQuantidadeAlunosPorSexo(sexo, false);
    }

    public Integer getQuantidadeAlunosPorSiatuacao(SituacaoDisciplina situacaoDisciplina) {
        return (int) getFrequenciaSituacoes().stream().filter(situacao -> situacao.getSituacaoDisciplina().equals(situacaoDisciplina)).count();
    }

    public Integer getQuantidadeAlunos(Boolean ignorarAusencia) {
        return (int) getAlunos().stream().filter(aluno -> !ignorarAusencia ||
            (ignorarAusencia && !obterFrequenciaSituacaoPorIdAluno(aluno.getId()).isAusencia())
        ).count();
    }

    public Integer getQuantidadeAlunos() {
        return getQuantidadeAlunos(false);
    }
}