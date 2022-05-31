package com.obervatorio_pedagogico.backend.domain.model.disciplina;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

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
public class Disciplina {

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
    
    @Column(name = "id_professor")
    private String professor; //TODO Criar objeto professor
    
    @ManyToMany
    @JoinTable(
            name = "t_disciplina_alunos",
            joinColumns = @JoinColumn (name = "id_disciplina"),
            inverseJoinColumns = @JoinColumn(name = "id_aluno")
    )
    private List<Aluno> alunos;

    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Nota> notas;

    public Boolean addAluno(Aluno aluno) {
        if (Objects.isNull(alunos))
            alunos = new ArrayList<>();
        if (!hasAlunos(aluno))
            return alunos.add(aluno);
        return false;
    }

    public Boolean removeAluno(Aluno aluno) {
        if (Objects.isNull(alunos))
            alunos = new ArrayList<>();
        return alunos.remove(aluno);
    }

    public Boolean hasAlunos(Aluno aluno) {
        return alunos.stream()
            .filter(alunoFiltro -> alunoFiltro.getId().equals(aluno.getId()) 
                || (alunoFiltro.getNome().equals(aluno.getNome())
                    && alunoFiltro.getMatricula().equals(aluno.getMatricula()))
            ).findFirst()
            .isPresent();
    }
}