package com.obervatorio_pedagogico.backend.domain.model.disciplina;

import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
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
@Table(name = "t_informacoesPeriodo")
public class InformacoesPeriodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "periodo_matriz")
    private String periodoMatriz;

    @Column(name = "preriodo_letivo")
    private String periodoLetivo;

    @ManyToOne
    @JoinColumn(name = "id_disciplina")
    private Disciplina disciplina;
    
    @Column(name = "id_professor")
    private String professor; //TODO Criar objeto professor
    
    @ManyToMany
    @JoinTable(
            name = "t_informacoesPeriodo_alunos",
            joinColumns = @JoinColumn (name = "id_informacoesPeriodo"),
            inverseJoinColumns = @JoinColumn(name = "id_aluno")
    )
    private List<Aluno> alunos;

    @OneToMany(mappedBy = "informacoesPeriodo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Nota> notas;
}
