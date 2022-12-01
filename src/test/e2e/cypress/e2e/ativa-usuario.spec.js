/// <reference types="cypress"/>

context('Ativa usuarios', () => {
    let token;
    
    it('Loga funcionario coped', () => {
        cy.request({
            method: 'POST',
            url: "/login",
            headers: {
                'Content-Type': 'application/json'
            },
            body: { 
                email: "thauan.amorim@academico.ifpb.edu.br", 
                senha: "123456", 
            },
            log: true ,
        }).then((response) => {
            expect(response.status).to.equal(200)
            token = response.body.data.token
        })
    });

    it('cadastrar funcionario coped com sucesso', () => {
        cy.request({
            method: 'POST',
            url: "/login/cadastrar",
            headers: {
                'Content-Type': 'application/json'
            },
            body: { 
                matricula: "000000000002", 
                email: "person-second@academico.ifpb.edu.br", 
                senha: "123456", 
                nome: "Second Person", 
                sexo: "MASCULINO", 
                tipo: "COPED" 
            },
            log: true,
        }).then(function(response){
            expect(response.status).to.equal(200)
        })
    });

    it('cadastrar professor com sucesso', () => {
        cy.request({
            method: 'POST',
            url: "/login/cadastrar",
            headers: {
                'Content-Type': 'application/json'
            },
            body: { 
                matricula: "000000000003", 
                email: "first-teacher@academico.ifpb.edu.br", 
                senha: "123456", 
                nome: "First Teacher", 
                sexo: "MASCULINO", 
                tipo: "PROFESSOR" 
            },
            log: true,
        }).then(function(response){
            expect(response.status).to.equal(200)
        })
    });

    it('Ativa funcionario coped com sucesso', () => {
        cy.request({
            method: 'POST',
            url: "/login/espera-cadastro/coped/ativar/2",
            headers: {
                'Content-Type': 'application/json',
                "Authorization": "Bearer " + token
            },
            log: true,
        }).then(function(response){
            expect(response.status).to.equal(200)
        })
    });

    it('Ativa funcionario coped com id não existente', () => {
        cy.request({
            method: 'POST',
            url: "/login/espera-cadastro/coped/ativar/5",
            headers: {
                'Content-Type': 'application/json',
                "Authorization": "Bearer " + token
            },
            log: true,
        }).then(function(response){
            expect(response.status).to.equal(400)
        })
    });

    it('Ativa funcionario coped com sucesso', () => {
        cy.request({
            method: 'POST',
            url: "/login/espera-cadastro/coped/ativar/2",
            headers: {
                'Content-Type': 'application/json',
                "Authorization": "Bearer " + token
            },
            log: true,
        }).then(function(response){
            expect(response.status).to.equal(200)
        })
    });

    it('Ativa professor com id não existente', () => {
        cy.request({
            method: 'POST',
            url: "/login/espera-cadastro/professor/ativar/5",
            headers: {
                'Content-Type': 'application/json',
                "Authorization": "Bearer " + token
            },
            log: true,
        }).then(function(response){
            expect(response.status).to.equal(400)
        })
    });

    it('Ativa professor com sucesso', () => {
        cy.request({
            method: 'POST',
            url: "/login/espera-cadastro/professor/ativar/1",
            headers: {
                'Content-Type': 'application/json',
                "Authorization": "Bearer " + token
            },
            log: true,
        }).then(function(response){
            expect(response.status).to.equal(200)
        })
    });

    it('Testa se funcionario coped foi ativado', () => {
        cy.request({
            method: 'POST',
            url: "/login",
            headers: {
                'Content-Type': 'application/json'
            },
            body: { 
                email: "person-second@academico.ifpb.edu.br", 
                senha: "123456"
            },
            log: true ,
        }).then((response) => {
            expect(response.status).to.equal(200)
            token = response.body.data.token
        })
    });

    it('Testa se funcionario professor foi ativado', () => {
        cy.request({
            method: 'POST',
            url: "/login",
            headers: {
                'Content-Type': 'application/json'
            },
            body: { 
                email: "first-teacher@academico.ifpb.edu.br", 
                senha: "123456"
            },
            log: true ,
        }).then((response) => {
            expect(response.status).to.equal(200)
            token = response.body.data.token
        })
    });
});