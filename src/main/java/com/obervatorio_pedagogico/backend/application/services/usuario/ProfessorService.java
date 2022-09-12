package com.obervatorio_pedagogico.backend.application.services.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.obervatorio_pedagogico.backend.domain.exceptions.NaoEncontradoException;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Professor;
import com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.usuario.ProfessorRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProfessorService {

    private ProfessorRepository ProfessorRepository;

    public Professor salvar(Professor professor) {
        return ProfessorRepository.save(professor);
    }

    public Optional<Professor> buscarPorEmailESenha(String email, String senha) {
        return ProfessorRepository.findProfessorByEmailAndSenha(email, senha);
    }

    public Optional<Professor> buscarPorEmail(String email) {
        return ProfessorRepository.findProfessorByEmail(email);
    }

    public Optional<Professor> buscarPorId(Long id) {
        return ProfessorRepository.findById(id);
    }

    public long count() {
        return ProfessorRepository.count();
    }

    public List<Professor> listarEsperaCadastro() {
        return ProfessorRepository.findProfessorWhereEsperaCadastroTrue();
    }

    public Professor ativarProfessor(Long id) {
        Optional<Professor> professorOp = buscarPorId(id);

        if (!professorOp.isPresent()) {
            throw new NaoEncontradoException();
        }

        professorOp.get().setAtivo(true);
        professorOp.get().setEsperaCadastro(false);
        return salvar(professorOp.get());
    }
}
