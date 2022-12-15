/// <reference types="cypress"/>

context('adiciona disciplina no professor', () => {
    let token;
    
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

    it('adiciona disciplina do professor com campo de codigos vazio', () => {
        cy.request({
            method: 'POST',
            url: "/funcionario/professor/adicionar-disciplina",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer '.concat(token),
                'token': token
            },
            body: {
                "idProfessor": 1,
                "codigos": []
            },
            log: true,
            failOnStatusCode: false
        }).then((response) => {
            expect(response.status).to.equal(400)
        })
    });

    it('adiciona disciplina do professor com campo de idProfessor nao existente', () => {
        cy.request({
            method: 'POST',
            url: "/funcionario/professor/adicionar-disciplina",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer '.concat(token),
                'token': token
            },
            body: {
                "idProfessor": 5,
                "codigos": ["TEC.0870", "TEC.0868"]
            },
            log: true,
            failOnStatusCode: false
        }).then((response) => {
            expect(response.status).to.equal(404)
        })
    });

    it('adiciona disciplina do professor com sucesso', () => {
        cy.request({
            method: 'POST',
            url: "/funcionario/professor/adicionar-disciplina",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer '.concat(token),
                'token': token
            },
            body: {
                "idProfessor": 1,
                "codigos": ["TEC.0870", "TEC.0868"]
            },
            log: true ,
        }).then((response) => {
            expect(response.status).to.equal(200)
        })
    });

    it('remove disciplinas do professor com campo de codigos vazio', () => {
        cy.request({
            method: 'POST',
            url: "/funcionario/professor/remover-disciplina",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer '.concat(token),
                'token': token
            },
            body: {
                "idProfessor": 1,
                "codigos": []
            },
            log: true,
            failOnStatusCode: false
        }).then((response) => {
            expect(response.status).to.equal(400)
        })
    });

    it('remove disciplina do professor com campo de idProfessor nao existente', () => {
        cy.request({
            method: 'POST',
            url: "/funcionario/professor/remover-disciplina",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer '.concat(token),
                'token': token
            },
            body: {
                "idProfessor": 5,
                "codigos": ["TEC.0870", "TEC.0868"]
            },
            log: true,
            failOnStatusCode: false
        }).then((response) => {
            expect(response.status).to.equal(404)
        })
    });

    it('remove disciplinas do professor com sucesso', () => {
        cy.request({
            method: 'POST',
            url: "/funcionario/professor/remover-disciplina",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer '.concat(token),
                'token': token
            },
            body: {
                "idProfessor": 1,
                "codigos": ["TEC.0870", "TEC.0868"]
            },
            log: true ,
        }).then((response) => {
            expect(response.status).to.equal(200)
        })
    });
});