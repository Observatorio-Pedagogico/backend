package com.obervatorio_pedagogico.backend.application.services.uploader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.obervatorio_pedagogico.backend.domain.model.extracao.ExtracaoThread;

public final class Uploader {
    private static Uploader instace;
    private List<ExtracaoThread> threads = new ArrayList<>();

    private Uploader() {}

    public static Uploader getInstance() {
        if (Objects.isNull(instace)) {
            instace = new Uploader();
        }
        return instace;
    }

    public boolean addThread(ExtracaoThread thread) {
        return threads.add(thread);
    }

    public void removeThread(Long extracaoId) {
        Optional<ExtracaoThread> extracaoThreadOp = threads.stream()
            .filter(extracaoThread -> extracaoThread.getExtracao().getId().equals(extracaoId))
            .findAny();

        if (extracaoThreadOp.isPresent()) {
            threads.remove(extracaoThreadOp.get());
        }
    }

    public Optional<ExtracaoThread> findById(Long id) {
        return threads.stream().filter(thread -> thread.getExtracao().getId().equals(id)).findFirst();
    }
}
