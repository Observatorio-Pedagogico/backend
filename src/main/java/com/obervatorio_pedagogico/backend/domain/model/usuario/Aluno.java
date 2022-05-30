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

import com.obervatorio_pedagogico.backend.domain.model.disciplina.InformacoesPeriodo;
import com.obervatorio_pedagogico.backend.domain.model.disciplina.Nota;
import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao;

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
    
    @ManyToMany(mappedBy = "alunos", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Extracao> extracoes;
    
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Nota> notas;
    
    @ManyToMany(mappedBy = "alunos", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<InformacoesPeriodo> informacoesPeriodos;

    public Boolean addExtracao(Extracao extracao) {
        if (Objects.isNull(extracoes))
            extracoes = new ArrayList<>();
        if (!hasExtracao(extracao))
            return extracoes.add(extracao);
        return false;
    }

    public Boolean removeExtracao(Extracao extracao) {
        if (Objects.isNull(extracoes))
            extracoes = new ArrayList<>();
        return extracoes.remove(extracao);
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

    public Boolean hasExtracao(Extracao extracao) {
        return extracoes.stream()
            .filter(extracaoFiltro -> extracaoFiltro.getId().equals(extracao.getId()) 
                || (extracaoFiltro.getTitulo().equals(extracao.getTitulo())
                    && extracaoFiltro.getPeriodoLetivo().equals(extracao.getPeriodoLetivo()))
            ).findFirst()
            .isPresent();
    }
    
    public Boolean hasNota(Nota nota) {
        return notas.stream()
            .filter(notaFiltro -> notaFiltro.getId().equals(nota.getId()) 
                || (notaFiltro.getValor().equals(nota.getValor())
                    && notaFiltro.getOrdem().equals(nota.getOrdem())
                    && notaFiltro.getTipo().equals(nota.getTipo())
                    && notaFiltro.getInformacoesPeriodo().getDisciplina().equals(nota.getInformacoesPeriodo().getDisciplina())
                    && notaFiltro.getInformacoesPeriodo().getPeriodoLetivo().equals(nota.getInformacoesPeriodo().getPeriodoLetivo()))
            ).findFirst()
            .isPresent();
    }
}
