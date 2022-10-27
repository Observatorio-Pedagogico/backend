
Feature: Testes de login de usuario

Background:
    * def urlBase = 'http://localhost:8080/observatorio-pedagogico/api'
    * def loginPath = '/login'
    * configure retry = { count: 1, interval: 1000 }

Scenario: Login email vazio
    * def path = urlBase + loginPath
    Given url path
    And header Content-Type = "application/json"
    And request { email: '', senha: '123456' }
    When method POST
    * print response
    Then assert responseStatus == 400
    * print response

Scenario: Login email fora instituicao  
    * def path = urlBase + loginPath
    Given url path
    And header Content-Type = "application/json"
    And request { email: 'thauan.amorim@gmail.com', senha: '123456' }
    When method POST
    * print response
    Then assert responseStatus == 400
    * print response

Scenario: Login senha vazio
    * def path = urlBase + loginPath
    Given url path
    And header Content-Type = "application/json"
    And request { email: 'thauan.amorim@academico.ifpb.edu.br', senha: '' }
    When method POST
    * print response
    Then assert responseStatus == 400
    * print response

Scenario: login de usuario com sucesso
    * def path = urlBase + loginPath
    Given url path
    And header Content-Type = "application/json"
    And request { email: 'thauan.amorim@academico.ifpb.edu.br', senha: '123456' }
    When method POST
    And eval if (responseStatus == 400) karate.call('Cadastra.feature', { matricula: "201915020008",  email: "thauan.amorim@academico.ifpb.edu.br",  senha: "123456",  nome: "Thauan Amorim",  sexo: "MASCULINO", tipo: "COPED" })
    And retry until responseStatus == 200
    * print response
    Then assert responseStatus == 200



