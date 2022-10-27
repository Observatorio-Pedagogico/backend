
@ignore @report=false
Feature: cadastra usuario para reusar

Background:
    * def urlBase = 'http://localhost:8080/observatorio-pedagogico/api'
    * def cadastrarPath = '/login/cadastrar'

Scenario: cadastra um usuario
    * def params = 
    """
        {
            'matricula': '#(matricula)', 
            'email': '#(email)', 
            'senha': '#(senha)', 
            'nome': '#(nome)', 
            'sexo': '#(sexo)', 
            'tipo': '#(tipo)'
        }
    """
    * def path = urlBase + cadastrarPath
    Given url path
    And header Content-Type = "application/json"
    And request params
    When method POST
    * print response
    Then status 200
