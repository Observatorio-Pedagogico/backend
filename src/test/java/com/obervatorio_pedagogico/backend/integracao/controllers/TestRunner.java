package com.obervatorio_pedagogico.backend.integracao.controllers;

import com.intuit.karate.junit5.Karate;

public class TestRunner {

    @Karate.Test
    Karate testFullPath() {
        return Karate.run("classpath:src/test/java/com/obervatorio_pedagogico/backend/controllers");
    }

    @Karate.Test
    Karate testCadastrar() {
        return Karate.run("CadastrarTest").relativeTo(getClass());
    }

    @Karate.Test
    Karate testLogin() {
        return Karate.run("LoginTest").relativeTo(getClass());
    }
}
