package com.obervatorio_pedagogico.backend.application.controllers.autentication;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obervatorio_pedagogico.backend.application.services.autenticacao.AutenticacaoService;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Usuario;
import com.obervatorio_pedagogico.backend.infrastructure.utils.httpResponse.ResponseService;
import com.obervatorio_pedagogico.backend.presentation.dto.auth.AuthResponse;
import com.obervatorio_pedagogico.backend.presentation.dto.auth.LoginRequest;
import com.obervatorio_pedagogico.backend.presentation.dto.usuario.UsuarioDto;
import com.obervatorio_pedagogico.backend.presentation.shared.Response;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/observatorio-pedagogico/api/login")
@AllArgsConstructor
@CrossOrigin
public class AutenticationController {

    private AutenticacaoService autenticacaoService;
    
    private ResponseService responseService;

    @PostMapping
    public ResponseEntity<Response<AuthResponse>> login(@RequestBody LoginRequest authRequest) {
        AuthResponse authResponse = autenticacaoService.login(authRequest);

        return responseService.ok(authResponse);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Response<Usuario>> cadastrar(@RequestBody UsuarioDto usuarioDto) {
        Usuario usuario = autenticacaoService.cadastrar(usuarioDto);

        return responseService.ok(usuario);
    }
}
