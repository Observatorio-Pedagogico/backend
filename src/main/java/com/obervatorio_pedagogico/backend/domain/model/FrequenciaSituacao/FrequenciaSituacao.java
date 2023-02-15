package com.obervatorio_pedagogico.backend.domain.model.FrequenciaSituacao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.obervatorio_pedagogico.backend.domain.model.disciplina.Disciplina;
import com.obervatorio_pedagogico.backend.domain.model.disciplina.Nota;
import com.obervatorio_pedagogico.backend.domain.model.disciplina.Nota.Tipo;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Aluno;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_frequencia_situacao")
public class FrequenciaSituacao implements Serializable {

    private static final List<SituacaoDisciplina> situacoesAusencia = Arrays.asList(SituacaoDisciplina.CANCELADO, SituacaoDisciplina.REPROVADO_POR_FALTA, SituacaoDisciplina.TRANCADO);

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "frequencia")
    private Integer frequencia;

    @ManyToMany(fetch = FetchType.LAZY,
    cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH
    })
    @JoinTable(
            name = "t_frequenciaSituacao_notas",
            joinColumns = @JoinColumn (name = "id_frequencia_situacao"),
            inverseJoinColumns = @JoinColumn(name = "id_nota")
    )
    private List<Nota> notas = new ArrayList<>();

    @Column(name = "situacao_disciplina")
    @Enumerated(EnumType.STRING)
    private SituacaoDisciplina situacaoDisciplina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aluno")
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_disciplina")
    private Disciplina disciplina;

    public boolean addNota(Nota nota) {
        if (Objects.isNull(notas))
            notas = new ArrayList<>();
        if (!hasNota(nota))
            return notas.add(nota);
        return false;
    }

    public boolean removeNota(Nota nota) {
        Integer tamanhoNotas = this.notas.size();
        this.notas = this.notas.stream().filter(not -> !not.getId().equals(nota.getId())).collect(Collectors.toList());
        return tamanhoNotas > this.notas.size();
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

    public Nota obterMedia() {
        return getNotas().stream().filter(nota -> nota.getTipo().equals(Tipo.MEDIA)).findFirst().orElse(null);
    }

    public Nota obterMenorNota() {
        Nota menorNota = null;

        for (Nota nota : getNotas()) {
            if (Objects.isNull(menorNota) || menorNota.getValor().longValue() > nota.getValor().longValue())
                menorNota = nota;
        }
        return menorNota;
    }

    public Nota obterMaiorNota() {
        Nota maiorNota = null;

        for (Nota nota : getNotas()) {
            if (Objects.isNull(maiorNota) || maiorNota.getValor().longValue() < nota.getValor().longValue())
                maiorNota = nota;
        }
        return maiorNota;
    }

    public enum SituacaoDisciplina {
        APROVADO("aprovado"), REPROVADO("reprovado"), TRANCADO("trancado"), REPROVADO_POR_FALTA("reprovado por falta"), CANCELADO("cancelado");

        private String text;

        SituacaoDisciplina(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }

        public static SituacaoDisciplina fromString(String text) {
            for (SituacaoDisciplina situacaoDisciplina : SituacaoDisciplina.values()) {
                if (situacaoDisciplina.text.equalsIgnoreCase(text)) {
                    return situacaoDisciplina;
                }
            }
            return null;
        }

        public Boolean isAusencia() {
            return FrequenciaSituacao.situacoesAusencia.contains(this);
        }
    }
}
