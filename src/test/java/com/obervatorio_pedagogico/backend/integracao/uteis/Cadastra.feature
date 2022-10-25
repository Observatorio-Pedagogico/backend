
Feature: cadastra usuario para reusar

Background:
    * def urlBase = 'http://localhost:8080/observatorio-pedagogico/api'
    * def cadastrarPath = '/login/cadastrar'

Scenario: As a <cadastraUser>: cadastra um usuario
    * def path = urlBase + cadastrarPath
    Given url path
    And header Content-Type = "application/json"
    And request { matricula: "201915020008", email: "thauan.amorim@academico.ifpb.edu.br", senha: "123456", nome: "Thauan Amorim", sexo: "MASCULINO", tipo: "COPED" }
    When method POST
    * print response
    Then status 200
