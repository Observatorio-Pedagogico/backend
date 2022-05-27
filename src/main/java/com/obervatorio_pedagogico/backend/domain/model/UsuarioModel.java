package com.obervatorio_pedagogico.backend.domain.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@MappedSuperclass
public abstract class UsuarioModel {

    @Column(name = "email")
    protected String email;

    @Column(name = "matricula")
    protected String matricula;

    @Column(name = "nome")
    protected String nome;
}
