package com.obervatorio_pedagogico.backend.application.services.usuario;

import com.obervatorio_pedagogico.backend.application.services.disciplina.DisciplinaService;
import com.obervatorio_pedagogico.backend.domain.exceptions.NaoEncontradoException;
import com.obervatorio_pedagogico.backend.domain.exceptions.OperacaoInvalidaException;
import com.obervatorio_pedagogico.backend.domain.model.disciplina.Disciplina;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Professor;
import com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.usuario.ProfessorRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Professor buscarPorId(Long id) {
        return ProfessorRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("ID professor"));
    }

    public synchronized void adicionarDisciplina(Long idProfessor, List<String> codigosDisciplinas) {
        if (codigosDisciplinas.isEmpty()) throw new OperacaoInvalidaException("codigosDisciplinas não informado");

        Professor professor = buscarPorId(idProfessor);
        List<Disciplina> disciplinas = disciplinaService.buscarPorCodigo(codigosDisciplinas);

        disciplinas.stream().forEach(disciplina -> professor.addDisciplina(disciplina));
        salvar(professor);
    }

    public synchronized void removerDisciplina(Long idProfessor, List<String> codigosDisciplinas) {
        if (codigosDisciplinas.isEmpty()) throw new OperacaoInvalidaException("codigosDisciplinas não informado");

        Professor professor = buscarPorId(idProfessor);
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
        Professor professor = buscarPorId(id);

        professor.setAtivo(true);
        return salvar(professor);
    }

    public Professor desativarProfessor(Long id) {
        Professor professor = buscarPorId(id);

        professor.setAtivo(false);
        professor.setEsperaCadastro(false);
        return salvar(professor);
    }
}
