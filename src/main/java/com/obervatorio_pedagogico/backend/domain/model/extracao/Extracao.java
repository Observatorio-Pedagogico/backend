package com.obervatorio_pedagogico.backend.domain.model.extracao;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import com.obervatorio_pedagogico.backend.domain.model.usuario.Aluno;
import com.obervatorio_pedagogico.backend.domain.model.usuario.FuncionarioCoped;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Professor;
import com.obervatorio_pedagogico.backend.presentation.model.usuario.EnvelopeFuncionario.TipoFuncionario;

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
public class Extracao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_funcionario_coped_remetente")
    private FuncionarioCoped funcionarioCopedRemetente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_professor_remetente")
    private Professor professorRemetente;

    @Column(name = "tipo_remetente")
    @Enumerated(EnumType.STRING)
    private TipoFuncionario tipoFuncionario;
    
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @Column(name = "ultima_data_hora_atualizacao")
    private LocalDateTime ultimaDataHoraAtualizacao;

    @ManyToMany(fetch = FetchType.LAZY,
    cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH
    })
    @JoinTable(
            name = "t_extracao_disciplina",
            joinColumns = @JoinColumn(name = "id_extracao"),
            inverseJoinColumns = @JoinColumn(name = "id_disciplina")
    )
    private List<Disciplina> disciplinas = new ArrayList<>();

    public Boolean addDisciplina(Disciplina disciplina) {
        if (!hasDisciplina(disciplina)) {
            return disciplinas.add(disciplina);
        }
        return false;
    }

    public Boolean removeDisciplina(Disciplina disciplina) {
        Integer tamanhoDisciplinas = this.disciplinas.size();
        this.disciplinas = this.disciplinas.stream().filter(disc -> !disc.getId().equals(disciplina.getId())).collect(Collectors.toList());
        return tamanhoDisciplinas > this.disciplinas.size();
    }

    public boolean hasDisciplina(Disciplina disciplina) {
        return disciplinas.stream()
        .anyMatch((disciplinaFiltro) -> { return
            disciplinaFiltro.getCodigo().equals(disciplina.getCodigo()) &&
            disciplinaFiltro.getPeriodoLetivo().equals(disciplina.getPeriodoLetivo());});
    }

    public Optional<Disciplina> findDisciplinaByCodigoEPeriodoLetivo(String codigo, String periodoLetivo) {
        return getDisciplinas().stream()
            .filter(disciplina -> disciplina.getCodigo().equals(codigo) && disciplina.getPeriodoLetivo().equals(periodoLetivo))
            .findFirst();
    }

    public Optional<Aluno> findAlunoByMatricula(String matricula) {
        return getDisciplinas().stream()
            .flatMap(disciplina -> disciplina.getAlunos().stream())
            .filter(aluno -> aluno.getMatricula().equals(matricula))
            .findAny();
    }

    public void iniciar() {
        this.setDataCadastro(LocalDateTime.now());
        this.setUltimaDataHoraAtualizacao(LocalDateTime.now());
        this.setStatus(Status.PROCESSANDO);
    }

    public boolean isStatusAtiva() {
        return status.isAtiva();
    }

    public boolean isStatusCancelada() {
        return status.isCancelada();
    }

    public boolean isProcessando() {
        return this.status.isProcessando();
    }

    public boolean isErro() {
        return this.status.isErro();
    }

    public enum Status {
        ATIVA, CANCELADA, PROCESSANDO, ERRO;

        public Boolean isAtiva() {
            return ATIVA.equals(this);
        }

        public Boolean isCancelada() {
            return CANCELADA.equals(this);
        }

        public Boolean isProcessando() {
            return PROCESSANDO.equals(this);
        }

        public Boolean isErro() {
            return ERRO.equals(this);
        }
    }
}
