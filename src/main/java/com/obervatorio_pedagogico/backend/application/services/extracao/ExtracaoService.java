package com.obervatorio_pedagogico.backend.application.services.extracao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.obervatorio_pedagogico.backend.application.services.usuario.AlunoService;
import com.obervatorio_pedagogico.backend.domain.exceptions.FormatoArquivoNaoSuportadoException;
import com.obervatorio_pedagogico.backend.domain.model.extracao.ExtracaoModel;
import com.obervatorio_pedagogico.backend.domain.model.usuario.AlunoModel;
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
    
    public ExtracaoModel cadastrar(ExtracaoRequest extracaoRequest) {
        processar(extracaoRequest.getArquivo());
        ExtracaoModel extracao = modelMapperService.convert(extracaoRequest, ExtracaoModel.class);
        return extracaoRepository.save(extracao);
    }

    public void processar(Arquivo arquivo) {
        validarArquivo(arquivo);
        List<AlunoModel> listaAlunos = lerArquivo(arquivo);
        alunoService.salvar(listaAlunos);
    }
    
    private void validarFormatoArquivo(Arquivo arquivo) {
        if (!arquivo.isSuportado())
            throw new FormatoArquivoNaoSuportadoException(arquivo.getConteudo().getContentType());
    }
    
    private void validarArquivo(Arquivo arquivo) {
        validarFormatoArquivo(arquivo);
    }

    private List<AlunoModel> lerArquivo(Arquivo arquivo) {
        List<AlunoModel> listaAlunos = Collections.emptyList();
        try {
            Workbook workbook = WorkbookFactory.create(arquivo.getConteudo().getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            listaAlunos = obterAlunosBySheet(sheet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaAlunos;
    }

    private List<AlunoModel> obterAlunosBySheet(Sheet sheet) {
        Iterator<Row> linhasInterator = sheet.iterator();
        Row linha;
        List<AlunoModel> listaAlunos = new ArrayList<AlunoModel>();

        while(linhasInterator.hasNext()) {
            linha = linhasInterator.next();

            if (linha.getRowNum() == 0 || linha.getCell(0).getStringCellValue().isEmpty())
                continue;

            AlunoModel aluno = new AlunoModel();
            aluno.setMatricula(linha.getCell(1).getStringCellValue());
            aluno.setNome(linha.getCell(2).getStringCellValue());

            listaAlunos.add(aluno);
        }
        return listaAlunos;
    }

}
