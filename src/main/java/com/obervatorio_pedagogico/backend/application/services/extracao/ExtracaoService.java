package com.obervatorio_pedagogico.backend.application.services.extracao;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.obervatorio_pedagogico.backend.application.services.disciplina.DisciplinaService;
import com.obervatorio_pedagogico.backend.application.services.uploader.Uploader;
import com.obervatorio_pedagogico.backend.application.services.usuario.AlunoService;
import com.obervatorio_pedagogico.backend.application.services.usuario.FuncionarioService;
import com.obervatorio_pedagogico.backend.domain.exceptions.FalhaArquivoException;
import com.obervatorio_pedagogico.backend.domain.exceptions.FormatoArquivoNaoSuportadoException;
import com.obervatorio_pedagogico.backend.domain.exceptions.NaoEncontradoException;
import com.obervatorio_pedagogico.backend.domain.model.FrequenciaSituacao.FrequenciaSituacao;
import com.obervatorio_pedagogico.backend.domain.model.FrequenciaSituacao.FrequenciaSituacao.SituacaoDisciplina;
import com.obervatorio_pedagogico.backend.domain.model.customMultipartFile.CustomMultipartFile;
import com.obervatorio_pedagogico.backend.domain.model.disciplina.Disciplina;
import com.obervatorio_pedagogico.backend.domain.model.disciplina.Nota;
import com.obervatorio_pedagogico.backend.domain.model.disciplina.Nota.Tipo;
import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao;
import com.obervatorio_pedagogico.backend.domain.model.extracao.Extracao.Status;
import com.obervatorio_pedagogico.backend.domain.model.extracao.ExtracaoThread;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Aluno;
import com.obervatorio_pedagogico.backend.domain.model.usuario.FuncionarioCoped;
import com.obervatorio_pedagogico.backend.domain.model.usuario.Professor;
import com.obervatorio_pedagogico.backend.infrastructure.persistence.repository.extracao.ExtracaoRepository;
import com.obervatorio_pedagogico.backend.infrastructure.rabbitmq.MQConfig;
import com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper.ModelMapperService;
import com.obervatorio_pedagogico.backend.presentation.dto.extracao.Arquivo;
import com.obervatorio_pedagogico.backend.presentation.dto.extracao.ExtracaoRequest;
import com.obervatorio_pedagogico.backend.presentation.model.arquivo.ArquivoAluno;
import com.obervatorio_pedagogico.backend.presentation.model.arquivo.ArquivoDisciplina;
import com.obervatorio_pedagogico.backend.presentation.model.arquivo.linhaArquivos.LinhaArquivoAluno;
import com.obervatorio_pedagogico.backend.presentation.model.arquivo.linhaArquivos.LinhaArquivoDisciplina;
import com.obervatorio_pedagogico.backend.presentation.model.queue.ArquivoQueue;
import com.obervatorio_pedagogico.backend.presentation.model.queue.ExtracaoRequestQueue;
import com.obervatorio_pedagogico.backend.presentation.model.usuario.EnvelopeFuncionario;
import com.obervatorio_pedagogico.backend.presentation.model.usuario.EnvelopeFuncionario.TipoFuncionario;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ExtracaoService {

    private final ExtracaoRepository extracaoRepository;

    private final AlunoService alunoService;

    private final DisciplinaService disciplinaService;

    private final ModelMapperService modelMapperService;

    private final FuncionarioService funcionarioService;

    private final RabbitTemplate rabbitTemplate;

    public void adicionarNaFila(ExtracaoRequest extracaoRequest) {
        Extracao extracao = modelMapperService.convert(extracaoRequest, Extracao.class);
        extracao.setStatus(Status.AGUARDANDO_PROCESSAMENTO);

        EnvelopeFuncionario envelopeFuncionario = funcionarioService.buscarFuncionarioByEmail(extracaoRequest.getEmailRemetente()).get();
        if (envelopeFuncionario.isFuncionarioCoped()) {
            extracao.setFuncionarioCopedRemetente((FuncionarioCoped) envelopeFuncionario.getFuncionario());
            extracao.setTipoFuncionario(TipoFuncionario.FUNCIONARIO_COPED);
        } else if (envelopeFuncionario.isProfessor()) {
            extracao.setProfessorRemetente((Professor) envelopeFuncionario.getFuncionario());
            extracao.setTipoFuncionario(TipoFuncionario.PROFESSOR);
        }

        ExtracaoRequestQueue extracaoRequestAux = modelMapperService.convert(extracao, ExtracaoRequestQueue.class);
        List<ArquivoQueue> arquivoQueues = new ArrayList<>();
        ArquivoQueue arquivoQueue;

        for(MultipartFile file : extracaoRequest.getArquivosMultipartFile()) {
            try {
                arquivoQueue = new ArquivoQueue(
                    file.getBytes(),
                    file.getContentType()
                );
    
                arquivoQueues.add(arquivoQueue);
            } catch (IOException e) {
                throw new FalhaArquivoException();
            }
        }
        
        // try {
        //     arquivoQueue = new ArquivoQueue(
        //         arquivo.getConteudo().getBytes(),
        //         arquivo.getConteudo().getContentType()
        //     );

        //     arquivoQueues.add(arquivoQueue);
        // } catch (NullPointerException nullPointerException) {
        //     arquivoDisciplinaQueue = null;
        // } catch (IOException e) {
        //     throw new FalhaArquivoException();
        // }

        // try {
        //     arquivoAlunoQueue = new ArquivoQueue(
        //         extracaoRequest.getArquivoAluno().getConteudo().getBytes(),
        //         extracaoRequest.getArquivoAluno().getConteudo().getContentType()
        //     );
        // } catch (NullPointerException nullPointerException) {
        //     arquivoAlunoQueue = null;
        // } catch (IOException e) {
        //     throw new FalhaArquivoException();
        // }
        
        extracaoRequestAux.setArquivoQueues(arquivoQueues);
        // extracaoRequestAux.setArquivoDisciplina(arquivoDisciplinaQueue);
        // extracaoRequestAux.setArquivoAluno(arquivoAlunoQueue);
        rabbitTemplate.convertAndSend(MQConfig.EXTRACAO_EXCHANGE, MQConfig.ROUTING_KEY_ENTRADA, extracaoRequestAux);
    }

    @RabbitListener(queues = {MQConfig.EXTRACAO_QUEUE_ENTRADA})
    public void cadastrar(ExtracaoRequestQueue extracaoRequestQueue) {
        Extracao extracao = modelMapperService.convert(extracaoRequestQueue, Extracao.class);
        ExtracaoRequest extracaoRequest = modelMapperService.convert(extracaoRequestQueue, ExtracaoRequest.class);
        Arquivo arquivo = null;

        extracaoRequest.setArquivos(new ArrayList<>());
        for (int i = 0; i < extracaoRequestQueue.getArquivoQueues().size(); i++) {
            CustomMultipartFile customMultipartFileArquivo = new CustomMultipartFile(extracaoRequestQueue.getArquivoQueues().get(i).getConteudoArquivo());
            arquivo = new Arquivo();
            arquivo.setConteudo(customMultipartFileArquivo);
            extracaoRequest.getArquivos().add(arquivo);
        }

        // try {
        //     CustomMultipartFile customMultipartFileArquivoDisciplina = new CustomMultipartFile(extracaoRequestQueue.getArquivoDisciplina().getConteudoArquivo());
        //     extracaoRequest.getArquivoDisciplina().setConteudo(customMultipartFileArquivoDisciplina);

        // } catch (NullPointerException nullPointerException) {}
        
        // try {
        //     CustomMultipartFile customMultipartFileArquivoAluno = new CustomMultipartFile(extracaoRequestQueue.getArquivoAluno().getConteudoArquivo());
        //     extracaoRequest.getArquivoAluno().setConteudo(customMultipartFileArquivoAluno);

        // } catch (NullPointerException nullPointerException) {}

        processar(extracao, extracaoRequest.getArquivos());
    }

    public void processar(Extracao extracao, List<Arquivo> arquivos) {
        validar(arquivos);
        lerArquivo(extracao, arquivos);
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

    private void validar(List<Arquivo> arquivos) {
        if (arquivos.size() < 2) {
            //TODO exception aqui
        }
        for (Arquivo arquivo : arquivos) {
            validarFormatoArquivo(arquivo);
        }
    }
    
    private void validarFormatoArquivo(Arquivo arquivo) {
        if (!arquivo.isSuportado())
            throw new FormatoArquivoNaoSuportadoException(arquivo.getConteudo().getContentType());
    }

    private void lerArquivo(Extracao extracao, List<Arquivo> arquivos ) {
        try {
            Workbook workbook = null;
            List<Sheet> sheets = new ArrayList<>();

            for (Arquivo arquivo : arquivos) {
                workbook = WorkbookFactory.create(arquivo.getConteudo().getInputStream());
                sheets.add(workbook.getSheetAt(0));
            }

            // if (Objects.nonNull(arquivoDisciplina)) {
            //     workbookDisciplina = 
            //     sheetDisciplina = 
            // }
            // if (Objects.nonNull(arquivoAluno)) {
            //     workbookAluno = WorkbookFactory.create(arquivoAluno.getConteudo().getInputStream());
            //     sheetAluno = workbookAluno.getSheetAt(0);
            // }
            
            iniciarThread(extracao, sheets);

            // if (Objects.nonNull(workbookDisciplina))
            //     workbookDisciplina.close();
            // if (Objects.nonNull(workbookAluno))
            //     workbookAluno.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    private void iniciarThread(Extracao extracao, List<Sheet> sheets) {
        ArquivoDisciplina arquivoDisciplina = null;
        ArquivoAluno arquivoAluno = null;
        
        ExtracaoThread thread = new ExtracaoThread();
        thread.setExtracao(extracao);
        Uploader.getInstance().addThread(thread);
        for (Sheet sheet : sheets) {
            if (ArquivoDisciplina.isConversivel(sheet)) {
                arquivoDisciplina = new ArquivoDisciplina(sheet);
            } else if (ArquivoAluno.isConversivel(sheet)) {
                arquivoAluno = new ArquivoAluno(sheet);
            }
            thread.setTotalLinhas(thread.getTotalLinhas() + sheet.getLastRowNum());
        }
        validarConversao(arquivoDisciplina, arquivoAluno);
        
        iniciarOperacao(extracao);
        cadastrarExtracaoDisciplina(extracao, arquivoDisciplina, thread);
        cadastrarExtracaoAluno(extracao, arquivoAluno, thread);
        salvarOperacao(extracao);
    }

    private void validarConversao(ArquivoDisciplina arquivoDisciplina, ArquivoAluno arquivoAluno) {
        if (Objects.isNull(arquivoDisciplina) || Objects.isNull(arquivoAluno)) {
            // TODO exception de erro na conversao do arquivo
            throw new RuntimeException("ERRO NA CONVERSAO DO ARQUIVOS");
        }
    }

    private void cadastrarExtracaoDisciplina(Extracao extracao, ArquivoDisciplina arquivoDisciplina, ExtracaoThread extracaoThread) {
        if (Objects.isNull(arquivoDisciplina)) return;

        Aluno aluno = null;
        Integer threadQuantidadeLinhas = extracaoThread.getLinhaAtual();

        for (int i = 0; i < arquivoDisciplina.getLinhas().size(); i++) {
            LinhaArquivoDisciplina linhaArquivo = arquivoDisciplina.getLinhas().get(i);
            Disciplina disciplina = findDisciplina(linhaArquivo, extracao);

            if (Objects.isNull(aluno) || arquivoDisciplina.proximoEMesmoAluno(i)) {
                if (Objects.nonNull(aluno)) {
                    disciplina.addAluno(aluno);
                    aluno.addDisciplina(disciplina);
                    disciplina.addExtracao(extracao);
                    extracao.addDisciplina(disciplina);
                    gerarFrequenciaSituacao(aluno, disciplina, linhaArquivo);
                    aluno = null;
                    extracaoThread.setLinhaAtual(threadQuantidadeLinhas + i);
                    continue;
                }
                aluno = findAluno(linhaArquivo, extracao);
                System.out.println(aluno.getNome());
            }
            if (Objects.nonNull(aluno)) {
                disciplina.addAluno(aluno);
                aluno.addDisciplina(disciplina);
                gerarFrequenciaSituacao(aluno, disciplina, linhaArquivo);
            }
            disciplina.addExtracao(extracao);
            extracao.addDisciplina(disciplina);
        }
    }

    private void cadastrarExtracaoAluno(Extracao extracao, ArquivoAluno arquivoAluno, ExtracaoThread extracaoThread) {
        if (Objects.isNull(arquivoAluno)) return;

        Aluno aluno = null;
        Integer threadQuantidadeLinhas = extracaoThread.getLinhaAtual();

        for (int i = 0; i < arquivoAluno.getLinhas().size(); i++) {
            LinhaArquivoAluno linhaArquivo = arquivoAluno.getLinhas().get(i);
            Disciplina disciplina = findDisciplina(linhaArquivo, extracao);

            if (Objects.isNull(aluno) || arquivoAluno.proximoEMesmoAluno(i)) {
                if (Objects.nonNull(aluno)) {
                    disciplina.addAluno(aluno);
                    aluno.addDisciplina(disciplina);
                    disciplina.addExtracao(extracao);
                    extracao.addDisciplina(disciplina);
                    gerarFrequenciaSituacao(aluno, disciplina, linhaArquivo);
                    aluno = null;
                    extracaoThread.setLinhaAtual(threadQuantidadeLinhas + i);
                    continue;
                }
                aluno = findAluno(linhaArquivo, extracao);
                System.out.println(aluno.getNome());
            }
            if (Objects.nonNull(aluno)) {
                disciplina.addAluno(aluno);
                aluno.addDisciplina(disciplina);
                gerarFrequenciaSituacao(aluno, disciplina, linhaArquivo);
            }
            disciplina.addExtracao(extracao);
            extracao.addDisciplina(disciplina);
        }
    }

    private Disciplina findDisciplina(LinhaArquivoDisciplina linhaArquivo, Extracao extracao) {
        String periodoLetivo = definirPeriodoLetivo(linhaArquivo.getAnoLetivo().toString(), linhaArquivo.getPeriodoLetivo().toString());
        Optional<Disciplina> disciplinaOp = extracao.findDisciplinaByCodigoEPeriodoLetivo(linhaArquivo.getCodigoDisciplina(), periodoLetivo);
        if (disciplinaOp.isPresent()) return disciplinaOp.get();

        disciplinaOp = disciplinaService.buscarPorCodigoEPeriodoLetivo(linhaArquivo.getCodigoDisciplina(), periodoLetivo);
        if (disciplinaOp.isPresent()) return disciplinaOp.get();

        Disciplina disciplina = new Disciplina();
        disciplina.setCodigo(linhaArquivo.getCodigoDisciplina());
        disciplina.setNome(linhaArquivo.getNomeDisciplina());
        disciplina.setPeriodoLetivo(periodoLetivo);
        return disciplina;
    }

    private Disciplina findDisciplina(LinhaArquivoAluno linhaArquivo, Extracao extracao) {
        String periodoLetivo = linhaArquivo.getPeriodo();
        Optional<Disciplina> disciplinaOp = extracao.findDisciplinaByCodigoEPeriodoLetivo(linhaArquivo.getCodigoDisciplina(), periodoLetivo);
        if (disciplinaOp.isPresent()) return disciplinaOp.get();

        disciplinaOp = disciplinaService.buscarPorCodigoEPeriodoLetivo(linhaArquivo.getCodigoDisciplina(), periodoLetivo);
        if (disciplinaOp.isPresent()) return disciplinaOp.get();

        Disciplina disciplina = new Disciplina();
        disciplina.setCodigo(linhaArquivo.getCodigoDisciplina());
        disciplina.setNome(linhaArquivo.getNomeDisciplina());
        disciplina.setPeriodoLetivo(periodoLetivo);
        return disciplina;
    }

    private Aluno findAluno(LinhaArquivoDisciplina linhaArquivo, Extracao extracao) {
        String matricula = linhaArquivo.getMatricula();

        Optional<Aluno> alunoOp = extracao.findAlunoByMatricula(matricula);
        if (alunoOp.isPresent()) return alunoOp.get();

        alunoOp = alunoService.buscarPorMatricula(matricula);
        if (alunoOp.isPresent()) {
            Aluno aluno = alunoOp.get();
            aluno.setNome(linhaArquivo.getNomeAluno());
            return aluno;
        }

        Aluno aluno = new Aluno();
        aluno.setMatricula(matricula);
        aluno.setNome(linhaArquivo.getNomeAluno());
        return aluno;
    }

    private Aluno findAluno(LinhaArquivoAluno linhaArquivo, Extracao extracao) {
        String matricula = linhaArquivo.getMatricula();

        Optional<Aluno> alunoOp = extracao.findAlunoByMatricula(matricula);
        if (alunoOp.isPresent()) return alunoOp.get();

        alunoOp = alunoService.buscarPorMatricula(matricula);
        if (alunoOp.isPresent()) return alunoOp.get();

        Aluno aluno = new Aluno();
        aluno.setMatricula(matricula);
        return aluno;
    }

    private void gerarFrequenciaSituacao(Aluno aluno, Disciplina disciplina, LinhaArquivoDisciplina linhaArquivoDisciplina) {
        FrequenciaSituacao frequenciaSituacao = buscarFrequenciaSituacao(aluno, disciplina);
        if (Objects.isNull(frequenciaSituacao)) {
            frequenciaSituacao = new FrequenciaSituacao();
            frequenciaSituacao.setDisciplina(disciplina);
            frequenciaSituacao.setAluno(aluno);
            aluno.addFrequenciaSituacoes(frequenciaSituacao);
            disciplina.addFrequenciaSituacoes(frequenciaSituacao);
        }

        frequenciaSituacao.setSituacaoDisciplina(SituacaoDisciplina.fromString(linhaArquivoDisciplina.getSituacao()));
    }

    private void gerarFrequenciaSituacao(Aluno aluno, Disciplina disciplina, LinhaArquivoAluno linhaArquivoAluno) {
        FrequenciaSituacao frequenciaSituacao = buscarFrequenciaSituacao(aluno, disciplina);
        if (Objects.isNull(frequenciaSituacao)) {
            frequenciaSituacao = new FrequenciaSituacao();
            frequenciaSituacao.setDisciplina(disciplina);
            frequenciaSituacao.setAluno(aluno);
            aluno.addFrequenciaSituacoes(frequenciaSituacao);
            disciplina.addFrequenciaSituacoes(frequenciaSituacao);
        }
        frequenciaSituacao.setFrequencia(linhaArquivoAluno.getFrequencia());

        List<Nota> notas = gerarNotas(linhaArquivoAluno.getNotas());
        for (Nota nota : notas) {
            nota.setAluno(aluno);
            nota.setDisciplina(disciplina);
        }
        frequenciaSituacao.setNotas(notas);
    }

    public FrequenciaSituacao buscarFrequenciaSituacao(Aluno aluno,  Disciplina disciplina) {
        return aluno.getFrequenciaSituacoes().stream()
            .filter(frequenciaSituacao -> frequenciaSituacao.getDisciplina().getCodigo().equals(disciplina.getCodigo()) &&
                frequenciaSituacao.getDisciplina().getPeriodoLetivo().equals(disciplina.getPeriodoLetivo()))
            .findFirst()
            .orElse(null);
    }

    private List<Nota> gerarNotas(String jsonNotas) {
        List<Nota> notas = new ArrayList<>();
        Nota nota = null;

        List<String> jsonSplitNotas = Arrays.asList(jsonNotas.split(","));

        for (String jsonSplitNota : jsonSplitNotas) {
            List<String> notaIndividual = Arrays.asList(jsonSplitNota.split(":"));

            nota = new Nota();
            switch (notaIndividual.get(0).charAt(0)) {
                case 'A':
                    nota.setTipo(Tipo.NOTA);
                    break;
                case 'M':
                    nota.setTipo(Tipo.MEDIA);
                    break;
                case 'F':
                    nota.setTipo(Tipo.FINAL);
                    break;
            }

            String ordem = notaIndividual.get(0).substring(1);
            if (!ordem.isEmpty())
                nota.setOrdem(new Integer(ordem));
            nota.setValor(new BigDecimal(notaIndividual.get(1)));

            notas.add(nota);
        }
        
        return notas;
    }

    private String definirPeriodoLetivo(String anoLetivo, String serieLetivo) {
        return anoLetivo.concat(".").concat(serieLetivo);
    }

    private void iniciarOperacao(Extracao extracao) {
        extracao.iniciar();
        extracaoRepository.save(extracao);
    }

    private void salvarOperacao(Extracao extracao) {
        extracao.setStatus(Status.SALVANDO);
        extracaoRepository.save(extracao);
        
        Uploader.getInstance().removeThread(extracao.getId());

        extracao = extracaoRepository.findById(extracao.getId()).get();
        extracao.setStatus(Status.ATIVA);
        extracaoRepository.save(extracao);
        System.out.println(Status.ATIVA);
    }
}
