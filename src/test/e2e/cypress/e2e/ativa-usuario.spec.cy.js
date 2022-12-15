/// <reference types="cypress"/>

context('Ativa usuarios', () => {
    let token;
    let professorId;
    
    it('Loga funcionario coped', () => {
        cy.request({
            method: 'POST',
            url: "/autenticacao/login",
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

    it('cadastrar professor com sucesso', () => {
        cy.request({
            method: 'POST',
            url: "/autenticacao/cadastrar",
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
            professorId = response.body.data.id
        })
    });

    it('Ativa professor com id não existente', () => {
        cy.request({
            method: 'POST',
            url: "/autenticacao/espera-cadastro/professor/ativar/5",
            headers: {
                'Content-Type': 'application/json',
                "Authorization": "Bearer " + token
            },
            log: true,
            failOnStatusCode: false
        }).then(function(response){
            expect(response.status).to.equal(404)
        })
    });

    it('Ativa professor com sucesso', () => {
        cy.request({
            method: 'POST',
            url: "/autenticacao/espera-cadastro/professor/ativar/".concat(professorId),
            headers: {
                'Content-Type': 'application/json',
                "Authorization": "Bearer " + token
            },
            log: true,
        }).then(function(response){
            expect(response.status).to.equal(200)
            expect(response.body.data.ativo).to.equal(true)
        })
    });
});