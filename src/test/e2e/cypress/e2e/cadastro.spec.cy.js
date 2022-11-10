/// <reference types="cypress"/>

context('Cadastro de Usuario Test', () => {
    it('cadastra usuario matricula vazio', () => {
        cy.request({
            method: 'POST',
            url: "/login/cadastrar",
            headers: {
                'Content-Type': 'application/json'
            },
            body: { matricula: "", email: "thauan.amorim@academico.ifpb.edu.br", senha: "123456", nome: "Thauan Amorim", sexo: "MASCULINO", tipo: "COPED" },
            log: true,
            failOnStatusCode: false
        }).then(function(response){
            expect(response.status).to.equal(400)
        })
    });

    it('cadastra usuario email vazio', () => {
        cy.request({
            method: 'POST',
            url: "/login/cadastrar",
            headers: {
                'Content-Type': 'application/json'
            },
            body: { matricula: "201915020008", email: "", senha: "123456", nome: "Thauan Amorim", sexo: "MASCULINO", tipo: "COPED" },
            log: true,
            failOnStatusCode: false
        }).then(function(response){
            expect(response.status).to.equal(400)
        })
    });

    it('cadastra usuario email fora instituicao', () => {
        cy.request({
            method: 'POST',
            url: "/login/cadastrar",
            headers: {
                'Content-Type': 'application/json'
            },
            body: { matricula: "201915020008", email: "thauan.amorim@gmail.com", senha: "123456", nome: "Thauan Amorim", sexo: "MASCULINO", tipo: "COPED" },
            log: true,
            failOnStatusCode: false
        }).then(function(response){
            expect(response.status).to.equal(400)
        })
    });

    it('cadastra usuario senha vazio', () => {
        cy.request({
            method: 'POST',
            url: "/login/cadastrar",
            headers: {
                'Content-Type': 'application/json'
            },
            body: { matricula: "201915020008", email: "thauan.amorim@gmail.com", senha: "", nome: "Thauan Amorim", sexo: "MASCULINO", tipo: "COPED" },
            log: true,
            failOnStatusCode: false
        }).then(function(response){
            expect(response.status).to.equal(400)
        })
    });

    it('cadastra um usuario nome vazio', () => {
        cy.request({
            method: 'POST',
            url: "/login/cadastrar",
            headers: {
                'Content-Type': 'application/json'
            },
            body: { matricula: "201915020008", email: "thauan.amorim@gmail.com", senha: "123456", nome: "", sexo: "MASCULINO", tipo: "COPED" },
            log: true,
            failOnStatusCode: false
        }).then(function(response){
            expect(response.status).to.equal(400)
        })
    });

    it('cadastra um usuario sexo vazio', () => {
        cy.request({
            method: 'POST',
            url: "/login/cadastrar",
            headers: {
                'Content-Type': 'application/json'
            },
            body: { matricula: "201915020008", email: "thauan.amorim@gmail.com", senha: "123456", nome: "Thauan Amorim", sexo: "", tipo: "COPED" },
            log: true,
            failOnStatusCode: false
        }).then(function(response){
            expect(response.status).to.equal(400)
        })
    });

    it('cadastra um usuario sexo qualquer outra coisa', () => {
        cy.request({
            method: 'POST',
            url: "/login/cadastrar",
            headers: {
                'Content-Type': 'application/json'
            },
            body: { matricula: "201915020008", email: "thauan.amorim@gmail.com", senha: "123456", nome: "Thauan Amorim", sexo: "teste", tipo: "COPED" },
            log: true,
            failOnStatusCode: false
        }).then(function(response){
            expect(response.status).to.equal(400)
        })
    });

    it('cadastra um usuario tipo vazio', () => {
        cy.request({
            method: 'POST',
            url: "/login/cadastrar",
            headers: {
                'Content-Type': 'application/json'
            },
            body: { matricula: "201915020008", email: "thauan.amorim@gmail.com", senha: "123456", nome: "Thauan Amorim", sexo: "MASCULINO", tipo: "" },
            log: true,
            failOnStatusCode: false
        }).then(function(response){
            expect(response.status).to.equal(400)
        })
    });

    it('cadastra um usuario tipo qualquer outra coisa', () => {
        cy.request({
            method: 'POST',
            url: "/login/cadastrar",
            headers: {
                'Content-Type': 'application/json'
            },
            body: { matricula: "201915020008", email: "thauan.amorim@gmail.com", senha: "123456", nome: "Thauan Amorim", sexo: "MASCULINO", tipo: "teste" },
            log: true,
            failOnStatusCode: false
        }).then(function(response){
            expect(response.status).to.equal(400)
        })
    });

    it('cadastrar usuario com sucesso', () => {
        cy.request({
            method: 'POST',
            url: "/login/cadastrar",
            headers: {
                'Content-Type': 'application/json'
            },
            body: { 
                matricula: "201915020009", 
                email: "thauan.amorim@academico.ifpb.edu.br", 
                senha: "123456", 
                nome: "Thauan Amorim", 
                sexo: "MASCULINO", 
                tipo: "COPED" 
            },
            log: true,
            failOnStatusCode: false
        }).then(function(response){
            expect(response.status).to.equal(200)
        })
    });
});