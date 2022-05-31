package com.obervatorio_pedagogico.backend.application.services.extracao;

import java.io.IOException;
import java.util.Iterator;

import com.obervatorio_pedagogico.backend.application.services.disciplina.DisciplinaService;
import com.obervatorio_pedagogico.backend.application.services.usuario.AlunoService;
import com.obervatorio_pedagogico.backend.domain.exceptions.DisciplinaNaoEncontradaException;
import com.obervatorio_pedagogico.backend.domain.exceptions.FormatoArquivoNaoSuportadoException;
import com.obervatorio_pedagogico.backend.domain.exceptions.UsuarioNaoEncontradoException;
import com.obervatorio_pedagogico.backend.domain.model.disciplina.Disciplina;
import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Aluno;
import com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.extracao.ExtracaoRepository;
import com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper.ModelMapperService;
import com.obervatorio_pedagogico.backend.presentation.dto.extracao.Arquivo;
import com.obervatorio_pedagogico.backend.presentation.dto.extracao.ExtracaoRequest;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import  org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ExtracaoService {

    private final ExtracaoRepository extracaoRepository;

    private final AlunoService alunoService;

    private final DisciplinaService disciplinaService;

    private final ModelMapperService modelMapperService;
    
    public Extracao cadastrar(ExtracaoRequest extracaoRequest) {
        Extracao extracao = modelMapperService.convert(extracaoRequest, Extracao.class);
        processar(extracao, extracaoRequest.getArquivo());
        return extracaoRepository.save(extracao);
    }

    public void processar(Extracao extracao, Arquivo arquivo) {
        validarArquivo(arquivo);
        lerArquivo(extracao, arquivo);
    }
    
    private void validarFormatoArquivo(Arquivo arquivo) {
        if (!arquivo.isSuportado())
            throw new FormatoArquivoNaoSuportadoException(arquivo.getConteudo().getContentType());
    }
    
    private void validarArquivo(Arquivo arquivo) {
        validarFormatoArquivo(arquivo);
    }

    private void lerArquivo(Extracao extracao, Arquivo arquivo) {
        try {
            Workbook workbook = WorkbookFactory.create(arquivo.getConteudo().getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            cadastrarExtracaoAlunosBySheet(extracao, sheet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cadastrarExtracaoAlunosBySheet(Extracao extracao, Sheet sheet) {
        Iterator<Row> linhasInterator = sheet.iterator();
        Row linha;

        while(linhasInterator.hasNext()) {
            linha = linhasInterator.next();

            if (linha.getRowNum() == 0 || linha.getCell(0).getStringCellValue().isEmpty())
                continue;

            Disciplina disciplina = cadastrarDisciplinaNaExtracao(linha);
            Aluno aluno = cadastrarAlunosNaExtracao(linha);

            disciplina.addAluno(aluno);
            aluno.addDisciplina(disciplina);
            
            extracao.addDisciplina(disciplina);

            disciplinaService.salvar(disciplina);
            alunoService.salvar(aluno);
            System.err.println(aluno.getNome());
        }
    }

    private Disciplina cadastrarDisciplinaNaExtracao(Row linha) {
        Disciplina disciplina;
        try {
            disciplina = disciplinaService.buscarPorCodigo(linha.getCell(4).getStringCellValue());
        } catch (DisciplinaNaoEncontradaException disciplinaNaoEncontradaException) {
            disciplina = new Disciplina();
            disciplina.setCodigo(linha.getCell(4).getStringCellValue());
            disciplina.setNome(linha.getCell(5).getStringCellValue());

            disciplina = disciplinaService.salvar(disciplina);
        }
        return disciplina;
    }

    private Aluno cadastrarAlunosNaExtracao(Row linha) {
        Aluno aluno;
        try {
            aluno = alunoService.buscarPorMatricula(linha.getCell(1).getStringCellValue());
        } catch (UsuarioNaoEncontradoException usuarioNaoEncontradoException) {
            aluno = new Aluno();
            aluno.setMatricula(linha.getCell(1).getStringCellValue());
            aluno.setNome(linha.getCell(2).getStringCellValue());

            aluno = alunoService.salvar(aluno);
        }
        return aluno;
    }
}
