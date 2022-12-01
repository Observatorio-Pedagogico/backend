/// <reference types="cypress"/>

context('Login de Usuario Test', () => {

    it('Login email vazio', () => {
        cy.request({
            method: 'POST',
            url: "/login",
            headers: {
                'Content-Type': 'application/json'
            },
            body: { 
                email: "", 
                senha: "123456", 
            },
            log: true,
            failOnStatusCode: false
        }).then(function(response){
            expect(response.status).to.equal(400)
        })
    });

    it('Login email fora instituicao', () => {
        cy.request({
            method: 'POST',
            url: "/login",
            headers: {
                'Content-Type': 'application/json'
            },
            body: { 
                email: "thauan.amorim@gmail.com", 
                senha: "123456", 
            },
            log: true,
            failOnStatusCode: false
        }).then(function(response){
            expect(response.status).to.equal(400)
        })
    });

    it('Login senha vazio', () => {
        cy.request({
            method: 'POST',
            url: "/login",
            headers: {
                'Content-Type': 'application/json'
            },
            body: { 
                email: "thauan.amorim@academico.ifpb.edu.br", 
                senha: "", 
            },
            log: true,
            failOnStatusCode: false
        }).then(function(response){
            expect(response.status).to.equal(400)
        })
    });

    it('Login usuario com sucesso', () => {
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
            log: true,
        }).then(function(response){
            expect(response.status).to.equal(200)
        })
    });

    it('Login usuario com sucesso', () => {
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
            log: true,
        }).then(function(response){
            expect(response.status).to.equal(200)
        })
    });
});