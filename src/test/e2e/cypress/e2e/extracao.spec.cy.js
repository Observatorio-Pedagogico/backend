/// <reference types="cypress" />

context('Extracao Test', () => {
    let token;

    it('Loga o usuario', () => {
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

    it('Envia uma extracao com titulo vazio', () => {
        const fileFormat = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        const formData = new FormData();

        formData.set('titulo', "");

        cy.fixture('alunos.xlsx', 'binary').then((alunoBin) => {
            let alunoBlob = Cypress.Blob.binaryStringToBlob(alunoBin, fileFormat)
            formData.append('arquivosMultipartFile', alunoBlob, 'alunos.xlsx');
        })
    
        cy.fixture('disciplinas.xlsx', 'binary').then((disciBin) => {
            let disciplinaBlob = Cypress.Blob.binaryStringToBlob(disciBin, fileFormat)
            formData.append('arquivosMultipartFile', disciplinaBlob, 'disciplinas.xlsx');
        })
        
        cy.form_request('POST', '/observatorio-pedagogico/api/extracao/enviar', "Bearer ".concat(token), formData, (response) => {
            if(response.status!==0){
                expect(response.status).to.equal(400)
            }
        });
    })

    it('Envia uma extracao sem arquivo de alunos', () => {
        const fileFormat = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        const formData = new FormData();

        formData.set('titulo', "Extracao teste");
    
        cy.fixture('disciplinas.xlsx', 'binary').then((disciBin) => {
            let disciplinaBlob = Cypress.Blob.binaryStringToBlob(disciBin, fileFormat)
            formData.append('arquivosMultipartFile', disciplinaBlob, 'disciplinas.xlsx');
        })
        
        cy.form_request('POST', '/observatorio-pedagogico/api/extracao/enviar', "Bearer ".concat(token), formData, (response) => {
            if(response.status!==0){
                expect(response.status).to.equal(400)
            }
        });
    })
    
    it('Envia uma extracao com sucesso', () => {
        const fileFormat = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        const formData = new FormData();

        formData.set('titulo', "Extracao teste");

        cy.fixture('alunos.xlsx', 'binary').then((alunoBin) => {
            let alunoBlob = Cypress.Blob.binaryStringToBlob(alunoBin, fileFormat)
            formData.append('arquivosMultipartFile', alunoBlob, 'alunos.xlsx');
        })
    
        cy.fixture('disciplinas.xlsx', 'binary').then((disciBin) => {
            let disciplinaBlob = Cypress.Blob.binaryStringToBlob(disciBin, fileFormat)
            formData.append('arquivosMultipartFile', disciplinaBlob, 'disciplinas.xlsx');
        })
        
        cy.form_request('POST', '/observatorio-pedagogico/api/extracao/enviar', "Bearer ".concat(token), formData, (response) => {
            if(response.status!==0){
                expect(response.status).to.equal(201)
            }
        });

        cy.wait(30000);
    })
});
