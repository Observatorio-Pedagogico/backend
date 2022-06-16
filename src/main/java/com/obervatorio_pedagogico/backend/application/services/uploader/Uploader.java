package com.obervatorio_pedagogico.backend.application.services.uploader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Uploader {
    private static Uploader instace;
    private List<Thread> threads;

    private Uploader() {}

    public static Uploader getInstance() {
        if (Objects.isNull(instace)) {
            instace = new Uploader();
        }
        return instace;
    }

    public boolean addThread(Thread thread) {
        if (Objects.isNull(threads)) {
            threads = new ArrayList<>();
        }
        return threads.add(thread);
    }
}
