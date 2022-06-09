package com.obervatorio_pedagogico.backend.application.services.extracao;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.obervatorio_pedagogico.backend.application.services.disciplina.DisciplinaService;
import com.obervatorio_pedagogico.backend.application.services.usuario.AlunoService;
import com.obervatorio_pedagogico.backend.domain.exceptions.FormatoArquivoNaoSuportadoException;
import com.obervatorio_pedagogico.backend.domain.model.disciplina.Disciplina;
import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Aluno;
import com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.extracao.ExtracaoRepository;
import com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper.ModelMapperService;
import com.obervatorio_pedagogico.backend.presentation.dto.extracao.Arquivo;
import com.obervatorio_pedagogico.backend.presentation.dto.extracao.ExtracaoRequest;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
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

    public List<Extracao> getTodos() throws NotFoundException{
        List<Extracao> extracoes = extracaoRepository.findAll();
        if(extracoes.isEmpty()){
            throw new NotFoundException();
        }
        return extracoes;
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

            cadastrarExtracao(extracao, sheet);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cadastrarExtracao(Extracao extracao, Sheet sheet) {
        Row linha;
        Aluno aluno = null;
        Disciplina disciplina = null;

        for (int i = 1; i < sheet.getLastRowNum(); i++) {
            linha = sheet.getRow(i);

            if (linha.getCell(0).getStringCellValue().isEmpty())
                continue;

            disciplina = cadastrarDisciplina(linha);

            if (
                (
                Objects.isNull(aluno) 
                || !aluno.getMatricula().equals(sheet.getRow(i+1).getCell(1).getStringCellValue())
                ) && i < sheet.getLastRowNum()-1)
            {
                if (Objects.nonNull(aluno)) {
                    aluno.addDisciplina(disciplina);
                    extracao.addDisciplina(disciplina);
                    disciplina.addAluno(aluno);
                    alunoService.salvar(aluno);
                    aluno = null;
                    continue;
                }
                aluno = cadastrarAluno(linha);
            }

            aluno.addDisciplina(disciplina);
            extracao.addDisciplina(disciplina);
            disciplina.addAluno(aluno);

            if (i >= sheet.getLastRowNum()-1) {
                alunoService.salvar(aluno);
            }
        }
    }

    private Disciplina cadastrarDisciplina(Row linha) {
        Optional<Disciplina> disciplinaOp = disciplinaService.buscarPorCodigoEPeriodoLetivo(linha.getCell(4).getStringCellValue(), linha.getCell(7).getStringCellValue());
        
        if (!disciplinaOp.isPresent()) {
            Disciplina disciplina = new Disciplina();
            disciplina.setCodigo(linha.getCell(4).getStringCellValue());
            disciplina.setNome(linha.getCell(5).getStringCellValue());
            disciplina.setPeriodoLetivo(linha.getCell(7).getStringCellValue());

            return disciplina;
        }
        return disciplinaOp.get();
    }

    private Aluno cadastrarAluno(Row linha) {
        Optional<Aluno> alunoOp = alunoService.buscarPorMatricula(linha.getCell(1).getStringCellValue());
        if (!alunoOp.isPresent()) {
            Aluno aluno = new Aluno();
            aluno.setMatricula(linha.getCell(1).getStringCellValue());
            aluno.setNome(linha.getCell(2).getStringCellValue());
            return aluno;
        }
        return alunoOp.get();
    }


}
