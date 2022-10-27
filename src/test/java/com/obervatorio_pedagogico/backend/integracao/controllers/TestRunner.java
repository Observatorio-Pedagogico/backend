package com.obervatorio_pedagogico.backend.integracao.controllers;

import com.intuit.karate.junit5.Karate;
import com.intuit.karate.junit5.Karate.Test;

public class TestRunner {

    @Test
    Karate testCadastrar() {
        return Karate.run("CadastrarTest").relativeTo(getClass());
    }

    @Test
    Karate testLogin() {
        return Karate.run("LoginTest").relativeTo(getClass());
    }

    // @Test
    // Karate testParallel() {
    //     return Karate.run("classpath:com/obervatorio_pedagogico/backend/integracao/controllers").relativeTo(getClass());
    // }
}
