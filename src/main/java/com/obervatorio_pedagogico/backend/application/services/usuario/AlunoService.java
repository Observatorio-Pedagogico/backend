package com.obervatorio_pedagogico.backend.application.services.usuario;

import java.util.ArrayList;
import java.util.List;

import com.obervatorio_pedagogico.backend.domain.model.usuario.AlunoModel;
import com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.usuario.AlunoRepository;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AlunoService {
    private final AlunoRepository alunoRepository;

    public AlunoModel salvar(AlunoModel aluno) {
        return alunoRepository.save(aluno);
    }

    public List<AlunoModel> salvar(List<AlunoModel> listaAlunos) {
        List<AlunoModel> listaAlunosSalvos = new ArrayList<AlunoModel>();
        for(AlunoModel aluno : listaAlunos) {
            listaAlunosSalvos.add(this.salvar(aluno));
        }
        return listaAlunosSalvos;
    }
}
