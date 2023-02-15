package com.obervatorio_pedagogico.backend.domain.model.usuario;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_funcionario_coped")
public class FuncionarioCoped extends Funcionario {

    @OneToMany(
        mappedBy = "funcionarioCopedRemetente", 
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Extracao> extracoes = new ArrayList<>();
    
    @Column(name = "cargo")
    @Enumerated(EnumType.STRING)
    private TipoCargo cargo;

    public enum TipoCargo {
        COORDENADORA;

        public boolean isCoordenadora(){
            return COORDENADORA.equals(this);
        }
    }
}
