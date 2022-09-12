package com.obervatorio_pedagogico.backend.application.controllers.autenticacao;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obervatorio_pedagogico.backend.application.services.autenticacao.AutenticacaoService;
import com.obervatorio_pedagogico.backend.domain.model.usuario.FuncionarioCoped;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Professor;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Usuario;
import com.obervatorio_pedagogico.backend.infrastructure.utils.httpResponse.ResponseService;
import com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper.ModelMapperService;
import com.obervatorio_pedagogico.backend.presentation.dto.auth.AuthResponse;
import com.obervatorio_pedagogico.backend.presentation.dto.auth.CadastroUsuarioDto;
import com.obervatorio_pedagogico.backend.presentation.dto.auth.LoginRequest;
import com.obervatorio_pedagogico.backend.presentation.dto.usuario.FuncionarioCopedDto;
import com.obervatorio_pedagogico.backend.presentation.dto.usuario.ProfessorDto;
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

    private ModelMapperService modelMapperService;

    @PostMapping
    public ResponseEntity<Response<AuthResponse>> login(@RequestBody LoginRequest authRequest) {
        AuthResponse authResponse = autenticacaoService.login(authRequest);

        return responseService.ok(authResponse);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Response<UsuarioDto>> cadastrar(@RequestBody CadastroUsuarioDto cadastroUsuarioDto) {
        Usuario usuario = autenticacaoService.cadastrar(cadastroUsuarioDto);

        UsuarioDto dto = modelMapperService.convert(usuario, UsuarioDto.class);

        return responseService.ok(dto);
    }

    @PostMapping("/espera-cadastro/coped/ativar/{id}")
    public ResponseEntity<Response<FuncionarioCopedDto>> ativarFuncionarioCoped(@PathVariable Long id) {
        FuncionarioCoped funcionarioCoped = autenticacaoService.ativarFuncionarioCoped(id);

        FuncionarioCopedDto dto = modelMapperService.convert(funcionarioCoped, FuncionarioCopedDto.class);
        
        return responseService.ok(dto);
    }

    @PostMapping("/espera-cadastro/professor/ativar/{id}")
    public ResponseEntity<Response<ProfessorDto>> ativarProfessor(@PathVariable Long id) {
        Professor professor = autenticacaoService.ativarProfessor(id);

        ProfessorDto dto = modelMapperService.convert(professor, ProfessorDto.class);
        
        return responseService.ok(dto);
    }

    // @GetMapping("/espera-cadastro/coped")
    // public ResponseEntity<Response<List<FuncionarioCopedDto>>> listarEsperaCadastroCoped() {
    //     List<FuncionarioCoped> funcionarioCopeds = autenticacaoService.listarEsperaCadastroCoped();

    //     List<FuncionarioCopedDto> dtos = modelMapperService.convertToList(funcionarioCopeds, FuncionarioCopedDto.class);

    //     return responseService.ok(funcionarioCopeds);
    // }

    // @GetMapping("/espera-cadastro/professor")
    // public ResponseEntity<Response<List<ProfessorDto>>> listarEsperaCadastroProfessor() {
    //     List<Professor> professors = autenticacaoService.listarEsperaCadastroProfessor();

    //     return responseService.ok(professors);
    // }
}
