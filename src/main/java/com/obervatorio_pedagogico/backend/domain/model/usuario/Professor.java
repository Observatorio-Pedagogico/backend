package com.obervatorio_pedagogico.backend.domain.model.usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

import com.obervatorio_pedagogico.backend.domain.model.disciplina.Disciplina;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "t_professor")
public class Professor extends Funcionario {
    
    @ManyToMany(fetch = FetchType.LAZY,
    cascade = {
            CascadeType.ALL
    })
    @JoinTable(
            name = "t_professor_disciplina",
            joinColumns = @JoinColumn(name = "id_professor"),
            inverseJoinColumns = @JoinColumn(name = "id_disciplina")
    )
    private List<Disciplina> disciplinas = new ArrayList<>();

    public boolean addDisciplina(Disciplina disciplina) {
        if (Objects.isNull(disciplina)) this.disciplinas = new ArrayList<>();
        if (hasDisciplina(disciplina)) return false;

        return disciplinas.add(disciplina);
    }

    public boolean removeDisciplina(Disciplina disciplina) {
        if (Objects.isNull(disciplina)) this.disciplinas = new ArrayList<>();

        return this.disciplinas.remove(disciplina);
    }

    public boolean hasDisciplina(Disciplina disciplina) {
        return disciplinas.stream().anyMatch(disciplinaList -> disciplinaList.getId().equals(disciplina.getId()));
    }
}
