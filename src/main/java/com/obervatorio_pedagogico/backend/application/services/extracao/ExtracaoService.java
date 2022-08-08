package com.obervatorio_pedagogico.backend.application.services.extracao;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.obervatorio_pedagogico.backend.application.services.disciplina.DisciplinaService;
import com.obervatorio_pedagogico.backend.application.services.uploader.Uploader;
import com.obervatorio_pedagogico.backend.application.services.usuario.AlunoService;
import com.obervatorio_pedagogico.backend.domain.exceptions.FalhaArquivoException;
import com.obervatorio_pedagogico.backend.domain.exceptions.FormatoArquivoNaoSuportadoException;
import com.obervatorio_pedagogico.backend.domain.exceptions.NaoEncontradoException;
import com.obervatorio_pedagogico.backend.domain.model.customMultipartFile.CustomMultipartFile;
import com.obervatorio_pedagogico.backend.domain.model.disciplina.Disciplina;
import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao;
import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao.Status;
import com.obervatorio_pedagogico.backend.domain.model.extracao.ExtracaoThread;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Aluno;
import com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.extracao.ExtracaoRepository;
import com.obervatorio_pedagogico.backend.infrastructure.rabbitmq.MQConfig;
import com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper.ModelMapperService;
import com.obervatorio_pedagogico.backend.presentation.dto.extracao.Arquivo;
import com.obervatorio_pedagogico.backend.presentation.dto.extracao.ArquivoQueue;
import com.obervatorio_pedagogico.backend.presentation.dto.extracao.ExtracaoRequest;
import com.obervatorio_pedagogico.backend.presentation.dto.extracao.ExtracaoRequestQueue;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ExtracaoService {

    private final ExtracaoRepository extracaoRepository;

    private final AlunoService alunoService;

    private final DisciplinaService disciplinaService;

    private final ModelMapperService modelMapperService;

    private final RabbitTemplate rabbitTemplate;

    public void adicionarNaFila(ExtracaoRequest extracaoRequest) {
        Extracao extracao = modelMapperService.convert(extracaoRequest, Extracao.class);
        extracao.setStatus(Status.AGUARDANDO_PROCESSAMENTO);

        ExtracaoRequestQueue extracaoRequestAux = modelMapperService.convert(extracao, ExtracaoRequestQueue.class);
        ArquivoQueue arquivoQueue;
        try {
            arquivoQueue = new ArquivoQueue(
                extracaoRequest.getArquivo().getConteudo().getBytes(),
                extracaoRequest.getArquivo().getConteudo().getContentType()
            );
        } catch (IOException e) {
            throw new FalhaArquivoException();
        }
        extracaoRequestAux.setArquivo(arquivoQueue);
        ExtracaoThread thread = new ExtracaoThread();
        thread.setExtracao(extracao);
        Uploader.getInstance().addThread(thread);

        rabbitTemplate.convertAndSend(MQConfig.EXTRACAO_EXCHANGE, MQConfig.ROUTING_KEY_ENTRADA, extracaoRequestAux);
    }

    @RabbitListener(queues = {MQConfig.EXTRACAO_QUEUE_ENTRADA})
    public void cadastrar(ExtracaoRequestQueue extracaoRequestQueue) {
        Extracao extracao = modelMapperService.convert(extracaoRequestQueue, Extracao.class);
        ExtracaoRequest extracaoRequest = modelMapperService.convert(extracaoRequestQueue, ExtracaoRequest.class);

        CustomMultipartFile customMultipartFile = new CustomMultipartFile(extracaoRequestQueue.getArquivo().getConteudoArquivo());

        extracaoRequest.getArquivo().setConteudo(customMultipartFile);
        processar(extracao, extracaoRequest.getArquivo());
    }

    public void processar(Extracao extracao, Arquivo arquivo) {
        validarArquivo(arquivo);
        lerArquivo(extracao, arquivo);
    }

    public List<Extracao> getTodos() {
        List<Extracao> extracoes = extracaoRepository.findAll();
        if(extracoes.isEmpty()){
            throw new NaoEncontradoException();
        }
        return extracoes;
    }

    public Extracao getById(Long id){
        Optional<Extracao> extracao = extracaoRepository.findById(id);
        if(!extracao.isPresent()){
            throw new NaoEncontradoException();
        }
        return extracao.get();
    }

    public List<Extracao> getByStatus(Status status){
        List<Extracao> extracoes = extracaoRepository.findByStatus(status);
        if(extracoes.isEmpty()){
            throw new NaoEncontradoException();
        }
        return extracoes;
    }

    public List<Extracao> getByPeriodoLetivo(String periodoLetivo){
        List<Extracao> extracoes = extracaoRepository.findByPeriodoLetivo(periodoLetivo);
        if(extracoes.isEmpty()){
            throw new NaoEncontradoException();
        }
        return extracoes;
    }

    public void deletaExtracao(Long id){
        if (Objects.isNull(id)) {
            throw new NaoEncontradoException("O id da extracao não pode ser null ");
        }

        Extracao extracao = extracaoRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("A extracao de ".concat(id.toString()).concat(" não foi encontrado.")));
        List<Disciplina> disciplinasExtracoesRemover = new ArrayList<>(extracao.getDisciplinas());
        disciplinasExtracoesRemover.stream().forEach(disciplina -> {
            disciplina.removeExtracao(extracao);
            extracao.removeDisciplina(disciplina);
            if (disciplina.isPassivoDeletar()) {
                disciplinaService.deleteDisciplina(disciplina);
            }
        });
        
        extracaoRepository.deleteById(extracao.getId());
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

            iniciarThread(extracao, sheet);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void iniciarThread(Extracao extracao, Sheet sheet) {
        ExtracaoThread thread;
        if (Uploader.getInstance().getThreads().size() > 0) {
            thread = Uploader.getInstance().getThreads().get(0);
            extracao = thread.getExtracao();
        } else {
            thread = new ExtracaoThread();
            thread.setExtracao(extracao);
            Uploader.getInstance().addThread(thread);
        }
        cadastrarExtracao(extracao, sheet, thread);

        // thread.setRunnable(
        //     new Runnable() {
        //         @Override
        //         public void run() {
        //             cadastrarExtracao(extracao, sheet, thread);
        //         }
        //     }
        // );
    }

    private void cadastrarExtracao(Extracao extracao, Sheet sheet, ExtracaoThread extracaoThread) {
        Row linha;
        Aluno aluno = null;
        extracaoThread.setTotalLinhas(sheet.getLastRowNum());

        extracao.setStatus(Status.ENVIANDO);
        extracao.setDataCadastro(LocalDateTime.now());
        extracao.setUltimaDataHoraAtualizacao(LocalDateTime.now());
        extracaoRepository.save(extracao);
        for (int i = 1; i < sheet.getLastRowNum(); i++) {
            linha = sheet.getRow(i);

            if (linha.getCell(0).getStringCellValue().isEmpty())
                continue;

            Disciplina disciplina = findDisciplina(linha, extracao);

            if (
                (
                Objects.isNull(aluno) 
                || !aluno.getMatricula().equals(sheet.getRow(i+1).getCell(1).getStringCellValue())
                ) && i < sheet.getLastRowNum()-1)
            {
                if (Objects.nonNull(aluno)) {
                    disciplina.addAluno(aluno);
                    aluno.addDisciplina(disciplina);
                    disciplina.addExtracao(extracao);
                    extracao.addDisciplina(disciplina);
                    aluno = null;
                    extracaoThread.setLinhaAtual(i);
                    continue;
                }
                aluno = findAluno(linha, extracao);
                System.out.println(aluno.getNome());
            }
            if (Objects.nonNull(aluno)) {
                disciplina.addAluno(aluno);
                aluno.addDisciplina(disciplina);
            }
            disciplina.addExtracao(extracao);
            extracao.addDisciplina(disciplina);
        }
        extracao.setStatus(Status.SALVANDO);
        extracaoRepository.save(extracao);
        
        Uploader.getInstance().removeThread(extracao.getId());

        extracao = extracaoRepository.findById(extracao.getId()).get();
        extracao.setStatus(Status.ATIVA);
        extracaoRepository.save(extracao);
        System.out.println(Status.ATIVA);
    }

    private Disciplina findDisciplina(Row linha, Extracao extracao) {
        Optional<Disciplina> disciplinaOp = extracao.findDisciplinaByCodigoEPeriodoLetivo(linha.getCell(4).getStringCellValue(), definirPeriodoLetivo(linha.getCell(7).getStringCellValue(), linha.getCell(8).getStringCellValue()));

        if (disciplinaOp.isPresent()) return disciplinaOp.get();

        disciplinaOp = disciplinaService.buscarPorCodigoEPeriodoLetivo(linha.getCell(4).getStringCellValue(), definirPeriodoLetivo(linha.getCell(7).getStringCellValue(), linha.getCell(8).getStringCellValue()));
        
        if (disciplinaOp.isPresent()) return disciplinaOp.get();

        Disciplina disciplina = new Disciplina();
        disciplina.setCodigo(linha.getCell(4).getStringCellValue());
        disciplina.setNome(linha.getCell(5).getStringCellValue());
        disciplina.setPeriodoLetivo(definirPeriodoLetivo(linha.getCell(7).getStringCellValue(), linha.getCell(8).getStringCellValue()));
        return disciplina;
    }

    private Aluno findAluno(Row linha, Extracao extracao) {
        Optional<Aluno> alunoOp = extracao.findAlunoByMatricula(linha.getCell(1).getStringCellValue());

        if (alunoOp.isPresent()) return alunoOp.get();

        alunoOp = alunoService.buscarPorMatricula(linha.getCell(1).getStringCellValue());

        if (alunoOp.isPresent()) return alunoOp.get();

        Aluno aluno = new Aluno();
        aluno.setMatricula(linha.getCell(1).getStringCellValue());
        aluno.setNome(linha.getCell(2).getStringCellValue());
        return aluno;
    }

    private String definirPeriodoLetivo(String anoLetivo, String serieLetivo) {
        return anoLetivo.concat(".").concat(serieLetivo);
    }
}
