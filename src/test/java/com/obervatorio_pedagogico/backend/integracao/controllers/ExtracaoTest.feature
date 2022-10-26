@ignore @report=false
Feature: testes de envio de extracao

Background:
    * def urlBase = 'http://localhost:8080/observatorio-pedagogico/api'
    * def extracaoPath = '/extracao/enviar'
    
Scenario: enviar extracao sucesso
    * def path = urlBase + extracaoPath
    * def myFeature = call read('Login.feature') 
    * def authToken = myFeature.accessToken
    Given url path
    And header Content-Type = "multipart/form-data"
    And header Authorization = 'Bearer ' + authToken
    And form field titulo = 'testando Extracao'
    And form field descricao = 'extracao ta indo'
    And form field periodoLetivo = '2021.1'
    And form field arquivosMultipartFile = "/home/JA/Estudando/Projetos/Observatorio-pedagogico/home/JA/Estudando/Projetos/Observatorio-pedagogico/Disciplinas.xlsx"
    And form field arquivosMultipartFile = "/home/JA/Estudando/Projetos/Observatorio-pedagogico/home/JA/Estudando/Projetos/Observatorio-pedagogico/alunos.xlsx"
    When method POST
    Then status 200
    * print response