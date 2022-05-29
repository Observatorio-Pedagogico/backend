package com.obervatorio_pedagogico.backend.domain.model.usuario;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import com.obervatorio_pedagogico.backend.domain.model.endereco.EnderecoModel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@MappedSuperclass
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "matricula", nullable = false)
    protected String matricula;

    @Column(name = "email")
    protected String email;

    @Column(name = "nome")
    protected String nome;

    @Column(name = "sexo")
    private Sexo sexo;

    @OneToOne
    @JoinColumn(name = "id_endereco")
    private EnderecoModel endereco;

    public enum Sexo {
        MASCULINO, FEMININO
    }
}
