package com.obervatorio_pedagogico.backend.application.services.extracao;

import java.io.IOException;
import java.util.Iterator;
import com.obervatorio_pedagogico.backend.application.services.usuario.AlunoService;
import com.obervatorio_pedagogico.backend.domain.exceptions.FormatoArquivoNaoSuportadoException;
import com.obervatorio_pedagogico.backend.domain.exceptions.UsuarioNaoEncontradoException;
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

            cadastrarAlunosNaExtracaoAlunosBySheet(extracao, sheet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cadastrarAlunosNaExtracaoAlunosBySheet(Extracao extracao, Sheet sheet) {
        Iterator<Row> linhasInterator = sheet.iterator();
        Row linha;

        while(linhasInterator.hasNext()) {
            linha = linhasInterator.next();

            if (linha.getRowNum() == 0 || linha.getCell(0).getStringCellValue().isEmpty())
                continue;

            Aluno aluno = null;
            try {
                aluno = alunoService.buscarPorMatricula(linha.getCell(1).getStringCellValue());
            } catch (UsuarioNaoEncontradoException usuarioNaoEncontradoException) {
                aluno = new Aluno();
                aluno.setMatricula(linha.getCell(1).getStringCellValue());
                aluno.setNome(linha.getCell(2).getStringCellValue());
            }
            aluno.addExtracao(extracao);
            extracao.addAluno(aluno);

            alunoService.salvar(aluno);
        }
    }
}
