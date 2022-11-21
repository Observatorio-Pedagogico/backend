package com.obervatorio_pedagogico.backend.application.controllers.autenticacao;

import com.obervatorio_pedagogico.backend.application.services.autenticacao.AutenticacaoService;
import com.obervatorio_pedagogico.backend.domain.model.usuario.FuncionarioCoped;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Professor;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Usuario;
import com.obervatorio_pedagogico.backend.infrastructure.utils.httpResponse.ResponseService;
import com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper.ModelMapperService;
import com.obervatorio_pedagogico.backend.presentation.dto.auth.request.CadastroUsuarioRequest;
import com.obervatorio_pedagogico.backend.presentation.dto.auth.request.LoginRequest;
import com.obervatorio_pedagogico.backend.presentation.dto.auth.response.AuthResponse;
import com.obervatorio_pedagogico.backend.presentation.dto.usuario.response.FuncionarioCopedResponse;
import com.obervatorio_pedagogico.backend.presentation.dto.usuario.response.ProfessorResponse;
import com.obervatorio_pedagogico.backend.presentation.dto.usuario.response.UsuarioResponse;
import com.obervatorio_pedagogico.backend.presentation.shared.Response;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/observatorio-pedagogico/api/login")
@AllArgsConstructor
@CrossOrigin
public class AutenticationController {

    private AutenticacaoService autenticacaoService;
    
    private ResponseService responseService;

    private ModelMapperService modelMapperService;

    @PostMapping
    public ResponseEntity<Response<AuthResponse>> login(@RequestBody @Valid LoginRequest authRequest) {
        AuthResponse authResponse = autenticacaoService.login(authRequest);

        return responseService.ok(authResponse);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Response<UsuarioResponse>> cadastrar(@RequestBody @Valid CadastroUsuarioRequest cadastroUsuarioDto) {
        Usuario usuario = autenticacaoService.cadastrar(cadastroUsuarioDto);

        UsuarioResponse dto = modelMapperService.convert(usuario, UsuarioResponse.class);

        return responseService.ok(dto);
    }

    @PostMapping("/espera-cadastro/coped/ativar/{id}")
    public ResponseEntity<Response<FuncionarioCopedResponse>> ativarFuncionarioCoped(@PathVariable Long id) {
        FuncionarioCoped funcionarioCoped = autenticacaoService.ativarFuncionarioCoped(id);

        FuncionarioCopedResponse dto = modelMapperService.convert(funcionarioCoped, FuncionarioCopedResponse.class);
        
        return responseService.ok(dto);
    }

    @PostMapping("/espera-cadastro/professor/ativar/{id}")
    public ResponseEntity<Response<ProfessorResponse>> ativarProfessor(@PathVariable Long id) {
        Professor professor = autenticacaoService.ativarProfessor(id);

        ProfessorResponse dto = modelMapperService.convert(professor, ProfessorResponse.class);
        
        return responseService.ok(dto);
    }

     @GetMapping("/espera-cadastro/coped")
     public ResponseEntity<Response<List<FuncionarioCopedResponse>>> listarEsperaCadastroCoped() {
         List<FuncionarioCoped> funcionarioCopeds = autenticacaoService.listarEsperaCadastroCoped();

         List<FuncionarioCopedResponse> dtos = modelMapperService.convert(funcionarioCopeds, FuncionarioCopedResponse.class);

         return responseService.ok(dtos);
     }

     @GetMapping("/espera-cadastro/professor")
     public ResponseEntity<Response<List<ProfessorResponse>>> listarEsperaCadastroProfessor() {
         List<Professor> professors = autenticacaoService.listarEsperaCadastroProfessor();

         List<ProfessorResponse> dtos = modelMapperService.convert(professors, ProfessorResponse.class);
         return responseService.ok(dtos);
     }
}
