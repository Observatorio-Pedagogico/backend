Feature: Testes de login de usuario

Background:
    * def urlBase = 'http://localhost:8080/observatorio-pedagogico/api'
    * def loginPath = '/login'
    * configure retry = { count: 1, interval: 1000 }

Scenario: login de usuario com sucesso
    * def path = urlBase + loginPath
    Given url path
    And header Content-Type = "application/json"
    And request { email: 'thauan.amorim@academico.ifpb.edu.br', senha: '123456' }
    When method POST
    And eval if (responseStatus == 400) karate.call('Cadastra.feature')
    And retry until responseStatus == 200
    * print response
    * def acessToken = response.data.token