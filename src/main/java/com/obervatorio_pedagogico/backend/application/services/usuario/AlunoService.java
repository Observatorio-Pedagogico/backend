package com.obervatorio_pedagogico.backend.application.services.usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.obervatorio_pedagogico.backend.domain.exceptions.NaoEncontradoException;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Aluno;
import com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.usuario.AlunoRepository;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AlunoService {
    private final AlunoRepository alunoRepository;

    public Aluno salvar(Aluno aluno) {
        return alunoRepository.save(aluno);
    }

    public List<Aluno> salvar(List<Aluno> listaAlunos) {
        List<Aluno> listaAlunosSalvos = new ArrayList<Aluno>();
        for(Aluno aluno : listaAlunos) {
            listaAlunosSalvos.add(this.salvar(aluno));
        }
        return listaAlunosSalvos;
    }

    public Optional<Aluno> buscarPorMatricula(String matricula) {
        Optional<Aluno> alunoSalvoOp = alunoRepository.findAlunoByMatricula(matricula);
        return alunoSalvoOp;
    }

    public void deleteById(Long id) {
        Aluno aluno = alunoRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("Não foi possivel encontrar o aluno de " + id + "."));
        alunoRepository.delete(aluno);
    }
}
