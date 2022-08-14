package com.obervatorio_pedagogico.backend.application.services.autenticacao;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.obervatorio_pedagogico.backend.application.services.usuario.FuncionarioCopedService;
import com.obervatorio_pedagogico.backend.application.services.usuario.ProfessorService;
import com.obervatorio_pedagogico.backend.application.services.usuario.UsuarioService;
import com.obervatorio_pedagogico.backend.application.services.utils.EmailService;
import com.obervatorio_pedagogico.backend.domain.exceptions.LoginInvalidoException;
import com.obervatorio_pedagogico.backend.domain.exceptions.UsuarioNaoPermitidoException;
import com.obervatorio_pedagogico.backend.domain.model.usuario.FuncionarioCoped;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Professor;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Usuario;
import com.obervatorio_pedagogico.backend.infrastructure.security.auth.JwtUtils;
import com.obervatorio_pedagogico.backend.infrastructure.security.auth.SharUtils;
import com.obervatorio_pedagogico.backend.infrastructure.security.service.SecurityService;
import com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper.ModelMapperService;
import com.obervatorio_pedagogico.backend.presentation.dto.auth.AuthResponse;
import com.obervatorio_pedagogico.backend.presentation.dto.auth.LoginRequest;
import com.obervatorio_pedagogico.backend.presentation.dto.usuario.UsuarioDto;
import com.obervatorio_pedagogico.backend.presentation.dto.usuario.UsuarioDto.Tipo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AutenticacaoService {

    private EmailService emailService;

    private SecurityService securityService;

    private FuncionarioCopedService funcionarioCopedService;

    private ProfessorService professorService;

    private UsuarioService usuarioService;

    private AuthenticationManager authenticationManager;

    private SharUtils sharUtils;

    private JwtUtils jwtUtils;

    private ModelMapperService modelMapperService;

    public AuthResponse login(LoginRequest authRequest) {
        
        if (!emailService.isDominioValido(authRequest.getEmail())) {
            throw new RuntimeException("Dominio inválido");
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

        Optional<Usuario> usuarioOp = usuarioService.buscarUsuarioByEmail(authRequest.getEmail());

        if (usuarioOp.isPresent() && !usuarioOp.get().isPermitido()) {
            throw new UsuarioNaoPermitidoException();
        }
        
        UserDetails loadedUser = securityService.loadUserByUsername(authRequest.getEmail());

        String token = jwtUtils.generateToken(loadedUser);

        return new AuthResponse(token);
    }

    public Usuario cadastrar(UsuarioDto usuarioDto) {
        this.validarCadastro(usuarioDto);

        boolean isPrimeiro = !usuarioService.existeUsuarioCadastrado();

        String senha = null;
        try {
            senha = sharUtils.shar256(usuarioDto.getSenha());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (usuarioDto.getTipo().equals(Tipo.COPED)) {
            FuncionarioCoped funcionarioCoped = modelMapperService.convert(usuarioDto, FuncionarioCoped.class);
            funcionarioCoped.setSenha(senha);

            if (isPrimeiro) {
                funcionarioCoped.setPermitido(true);
                funcionarioCoped.setEsperaCadastro(false);
            }

            return funcionarioCopedService.salvar(funcionarioCoped);
        } else {
            Professor professor = modelMapperService.convert(usuarioDto, Professor.class);
            professor.setSenha(senha);

            if (isPrimeiro) {
                professor.setPermitido(true);
                professor.setEsperaCadastro(false);
            }

            return professorService.salvar(professor);
        }
    }

    private void validarCadastro(UsuarioDto usuarioDto) {
        if (!emailService.isDominioValido(usuarioDto.getEmail())) {
            throw new RuntimeException("Dominio inválido");
        }

        if (
            professorService.buscarPorEmail(usuarioDto.getEmail()).isPresent() || 
            funcionarioCopedService.buscarPorEmail(usuarioDto.getEmail()).isPresent()
        ) {
            throw new RuntimeException("Já existe");
        }
    }
}
