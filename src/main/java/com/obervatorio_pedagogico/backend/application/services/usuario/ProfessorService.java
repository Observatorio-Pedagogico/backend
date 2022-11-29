package com.obervatorio_pedagogico.backend.application.services.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.obervatorio_pedagogico.backend.application.services.disciplina.DisciplinaService;
import com.obervatorio_pedagogico.backend.domain.exceptions.NaoEncontradoException;
import com.obervatorio_pedagogico.backend.domain.exceptions.OperacaoInvalidaException;
import com.obervatorio_pedagogico.backend.domain.model.disciplina.Disciplina;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Professor;
import com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.usuario.ProfessorRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProfessorService {

    private ProfessorRepository ProfessorRepository;

    private DisciplinaService disciplinaService;

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

    public void adicionarDisciplina(Long idProfessor, List<String> codigosDisciplinas) {
        if (codigosDisciplinas.isEmpty()) throw new OperacaoInvalidaException("codigosDisciplinas não informado");

        Professor professor = buscarPorId(idProfessor).orElseThrow(() -> new NaoEncontradoException("Id professor"));
        List<Disciplina> disciplinas = disciplinaService.buscarPorCodigo(codigosDisciplinas);

        disciplinas.stream().forEach(disciplina -> professor.addDisciplina(disciplina));
        salvar(professor);
    }

    public void removerDisciplina(Long idProfessor, List<String> codigosDisciplinas) {
        if (codigosDisciplinas.isEmpty()) throw new OperacaoInvalidaException("codigosDisciplinas não informado");

        Professor professor = buscarPorId(idProfessor).orElseThrow(() -> new NaoEncontradoException("Id professor"));
        List<Disciplina> disciplinas = disciplinaService.buscarPorCodigo(codigosDisciplinas);

        disciplinas.stream().forEach(disciplina -> professor.removeDisciplina(disciplina));
        salvar(professor);
    }

    public long count() {
        return ProfessorRepository.count();
    }

    public Page<Professor> listarEsperaCadastro(Pageable pageable) {
        return ProfessorRepository.findProfessorWhereEsperaCadastroTrue(pageable);
    }

    public Page<Professor> listar(Pageable pageable) {
        return ProfessorRepository.findProfessorWhereEsperaCadastroFalse(pageable);
    }

    public Professor ativarProfessorEsperaCadastro(Long id) {
        Professor professor = ativarProfessor(id);
        professor.setEsperaCadastro(false);

        return salvar(professor);
    }

    public Professor desativarProfessorEsperaCadastro(Long id) {
        Professor professor = desativarProfessor(id);
        professor.setEsperaCadastro(false);

        return salvar(professor);
    }
    
    public Professor ativarProfessor(Long id) {
        Optional<Professor> professorOp = buscarPorId(id);

        if (!professorOp.isPresent()) {
            throw new NaoEncontradoException();
        }

        professorOp.get().setAtivo(true);
        return salvar(professorOp.get());
    }

    public Professor desativarProfessor(Long id) {
        Optional<Professor> professorOp = buscarPorId(id);

        if (!professorOp.isPresent()) {
            throw new NaoEncontradoException();
        }

        professorOp.get().setAtivo(false);
        professorOp.get().setEsperaCadastro(false);
        return salvar(professorOp.get());
    }
}
