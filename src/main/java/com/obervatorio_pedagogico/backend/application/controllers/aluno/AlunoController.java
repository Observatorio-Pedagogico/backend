package com.obervatorio_pedagogico.backend.application.controllers.aluno;

import com.obervatorio_pedagogico.backend.application.services.usuario.AlunoService;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Aluno;
import com.obervatorio_pedagogico.backend.infrastructure.utils.httpResponse.ResponseService;
import com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper.ModelMapperService;
import com.obervatorio_pedagogico.backend.presentation.dto.usuario.response.AlunoResumidoResponse;
import com.obervatorio_pedagogico.backend.presentation.shared.Response;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/observatorio-pedagogico/api/aluno")
@AllArgsConstructor
@CrossOrigin
public class AlunoController {
    
    private AlunoService alunoService;

    private ResponseService responseService;

    private ModelMapperService modelMapperService;

    @GetMapping("/resumido")
    public ResponseEntity<Response<Page<AlunoResumidoResponse>>> buscarAlunos(Pageable pageable,
        @RequestParam(value = "periodoLetivo", required = false) List<String> periodoLetivo,
        @RequestParam(value = "codigo", required = false) List<String> codigo,
        @RequestParam(value = "ignorarAusencia", required = false) Boolean ignorarAusencia
    ) {
        if (Objects.isNull(ignorarAusencia)) ignorarAusencia = false;
        if (Objects.isNull(codigo)) codigo = new ArrayList<>();
        if (Objects.isNull(periodoLetivo)) periodoLetivo = new ArrayList<>();

        Page<Aluno> alunos = alunoService.buscar(pageable, codigo, periodoLetivo, ignorarAusencia);

        return responseService.ok(modelMapperService.convert(alunos, AlunoResumidoResponse.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<AlunoResumidoResponse>> buscarAlunoPorId(@PathVariable Long id) {
        Aluno aluno = alunoService.buscarPorId(id);

        return responseService.ok(modelMapperService.convert(aluno, AlunoResumidoResponse.class));
    }
}
