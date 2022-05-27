package com.obervatorio_pedagogico.backend.domain.model.usuario;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@MappedSuperclass
public abstract class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "email")
    protected String email;

    @Column(name = "matricula")
    protected String matricula;

    @Column(name = "nome")
    protected String nome;

    @Column(name = "sexo")
    private Sexo sexo;

    public enum Sexo {
        MASCULINO, FEMININO
    }
}
