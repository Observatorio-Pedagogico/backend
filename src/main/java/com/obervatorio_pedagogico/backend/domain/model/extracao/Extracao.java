package com.obervatorio_pedagogico.backend.domain.model.extracao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

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
@Table(name = "t_extracao")
public class Extracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "periodo_letivo")
    private String periodoLetivo;
    
    @Column(name = "ultima_data_hora_atualizacao")
    private LocalDateTime ultimaDataHoraAtualizacao;

    @ManyToMany
    @JoinTable(
            name = "t_extracao_disciplina",
            joinColumns = @JoinColumn(name = "id_extracao"),
            inverseJoinColumns = @JoinColumn(name = "id_disciplina")
    )
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

    public Boolean hasDisciplina(Disciplina disciplina) {
        return disciplinas.stream()
            .filter(disciplinaFiltro -> disciplinaFiltro.getNome().equals(disciplina.getNome()))
            .findFirst()
            .isPresent();
    } // TODO adicionar has by id

    public Boolean isStatusAtiva() {
        return status.isAtiva();
    }

    public Boolean isStatusCancelada() {
        return status.isCancelada();
    }

    public enum Status {
        ATIVA, CANCELADA;

        public Boolean isAtiva() {
            return ATIVA.equals(this);
        }

        public Boolean isCancelada() {
            return CANCELADA.equals(this);
        }
    }
}
