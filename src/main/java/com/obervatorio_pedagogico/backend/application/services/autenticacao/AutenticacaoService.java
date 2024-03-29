package com.obervatorio_pedagogico.backend.application.services.autenticacao;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.obervatorio_pedagogico.backend.application.services.usuario.FuncionarioCopedService;
import com.obervatorio_pedagogico.backend.application.services.usuario.FuncionarioService;
import com.obervatorio_pedagogico.backend.application.services.usuario.ProfessorService;
import com.obervatorio_pedagogico.backend.application.services.utils.EmailService;
import com.obervatorio_pedagogico.backend.domain.exceptions.LoginInvalidoException;
import com.obervatorio_pedagogico.backend.domain.exceptions.UsuarioEmailDominioInvalido;
import com.obervatorio_pedagogico.backend.domain.exceptions.UsuarioJaExistenteException;
import com.obervatorio_pedagogico.backend.domain.exceptions.UsuarioNaoPermitidoException;
import com.obervatorio_pedagogico.backend.domain.model.usuario.FuncionarioCoped;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Professor;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Usuario;
import com.obervatorio_pedagogico.backend.infrastructure.security.auth.JwtUtils;
import com.obervatorio_pedagogico.backend.infrastructure.security.auth.SharUtils;
import com.obervatorio_pedagogico.backend.infrastructure.security.service.SecurityService;
import com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper.ModelMapperService;
import com.obervatorio_pedagogico.backend.presentation.dto.auth.request.CadastroUsuarioRequest;
import com.obervatorio_pedagogico.backend.presentation.dto.auth.request.CadastroUsuarioRequest.Tipo;
import com.obervatorio_pedagogico.backend.presentation.dto.auth.request.LoginRequest;
import com.obervatorio_pedagogico.backend.presentation.dto.auth.response.AuthResponse;
import com.obervatorio_pedagogico.backend.presentation.model.usuario.EnvelopeFuncionario;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AutenticacaoService {

    private EmailService emailService;

    private SecurityService securityService;

    private FuncionarioCopedService funcionarioCopedService;

    private ProfessorService professorService;

    private FuncionarioService funcionarioService;

    private AuthenticationManager authenticationManager;

    private SharUtils sharUtils;

    private JwtUtils jwtUtils;

    private ModelMapperService modelMapperService;

    public AuthResponse login(LoginRequest authRequest) {
        
        if (!emailService.isDominioValido(authRequest.getEmail())) {
            throw new UsuarioEmailDominioInvalido();
        }

        String senhaShar = null;
        try {
            senhaShar = sharUtils.shar256(authRequest.getSenha());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), senhaShar));
        } catch (AuthenticationException authenticationException) {
            throw new LoginInvalidoException();
        }

        Optional<EnvelopeFuncionario> usuarioOp = funcionarioService.buscarFuncionarioPorEmail(authRequest.getEmail());

        if (usuarioOp.isPresent() && !usuarioOp.get().getFuncionario().isAtivo()) {
            throw new UsuarioNaoPermitidoException();
        }
        
        UserDetails loadedUser = securityService.loadUserByUsername(authRequest.getEmail());

        String token = jwtUtils.generateToken(loadedUser);

        return new AuthResponse(token);
    }

    public Usuario cadastrar(CadastroUsuarioRequest cadastroUsuarioDto) {
        this.validarCadastro(cadastroUsuarioDto);

        boolean isPrimeiro = !funcionarioService.existeFuncionarioCadastrado();

        String senha = null;
        try {
            senha = sharUtils.shar256(cadastroUsuarioDto.getSenha());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (cadastroUsuarioDto.getTipo().equals(Tipo.COPED)) {
            FuncionarioCoped funcionarioCoped = modelMapperService.convert(cadastroUsuarioDto, FuncionarioCoped.class);
            funcionarioCoped.setSenha(senha);

            if (isPrimeiro) {
                funcionarioCoped.setAtivo(true);
                funcionarioCoped.setEsperaCadastro(false);
            }

            return funcionarioCopedService.salvar(funcionarioCoped);
        } else {
            Professor professor = modelMapperService.convert(cadastroUsuarioDto, Professor.class);
            professor.setSenha(senha);

            if (isPrimeiro) {
                professor.setAtivo(true);
                professor.setEsperaCadastro(false);
            }

            return professorService.salvar(professor);
        }
    }

    public FuncionarioCoped ativarFuncionarioCopedEsperaCadastro(Long id) {
        return funcionarioCopedService.ativarFuncionarioCopedEsperaCadastro(id);
    }

    public FuncionarioCoped desativarFuncionarioCopedEsperaCadastro(Long id) {
        return funcionarioCopedService.desativarFuncionarioCopedEsperaCadastro(id);
    }

    public Professor ativarProfessorEsperaCadastro(Long id) {
        return professorService.ativarProfessorEsperaCadastro(id);
    }

    public Professor desativarProfessorEsperaCadastro(Long id) {
        return professorService.desativarProfessorEsperaCadastro(id);
    }

    public Page<FuncionarioCoped> listarCopedEsperaCadastro(Pageable pageable) {
        return funcionarioCopedService.listarEsperaCadastro(pageable);
    }

    public Page<Professor> listarProfessorEsperaCadastro(Pageable pageable) {
        return professorService.listarEsperaCadastro(pageable);
    }

    public FuncionarioCoped ativarFuncionarioCoped(Long id) {
        return funcionarioCopedService.ativarFuncionarioCoped(id);
    }

    public FuncionarioCoped desativarFuncionarioCoped(Long id) {
        return funcionarioCopedService.desativarFuncionarioCoped(id);
    }

    public Professor ativarProfessor(Long id) {
        return professorService.ativarProfessor(id);
    }

    public Professor desativarProfessor(Long id) {
        return professorService.desativarProfessor(id);
    }

    public Page<FuncionarioCoped> listarCoped(Pageable pageable) {
        return funcionarioCopedService.listar(pageable);
    }

    public Page<Professor> listarProfessor(Pageable pageable) {
        return professorService.listar(pageable);
    }

    private void validarCadastro(CadastroUsuarioRequest cadastroUsuarioDto) {
        if (!emailService.isDominioValido(cadastroUsuarioDto.getEmail())) {
            throw new UsuarioEmailDominioInvalido();
        }

        if (
            professorService.buscarPorEmail(cadastroUsuarioDto.getEmail()).isPresent() || 
            funcionarioCopedService.buscarPorEmail(cadastroUsuarioDto.getEmail()).isPresent()
        ) {
            throw new UsuarioJaExistenteException();
        }
    }
}
