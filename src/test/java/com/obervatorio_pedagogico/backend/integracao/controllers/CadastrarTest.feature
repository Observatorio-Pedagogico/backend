
Feature: testes de cadastro de usuario

Background:
    * def urlBase = 'http://localhost:8080/observatorio-pedagogico/api'
    * def cadastrarPath = '/login/cadastrar'
    * def esperaCadastroPath = '/espera-cadastro/coped/ativar/'

Scenario: cadastra usuario matricula vazio
    * def path = urlBase + cadastrarPath
    Given url path
    And header Content-Type = "application/json"
    And request { matricula: "", email: "thauan.amorim@academico.ifpb.edu.br", senha: "123456", nome: "Thauan Amorim", sexo: "MASCULINO", tipo: "COPED" }
    When method POST
    Then status 400
    * print response

Scenario: cadastra um usuario com matricula com texto
    * def path = urlBase + cadastrarPath
    Given url path
    And header Content-Type = "application/json"
    And request { matricula: "", email: "thauan.amorim@academico.ifpb.edu.br", senha: "123456", nome: "Thauan Amorim", sexo: "MASCULINO", tipo: "COPED" }
    When method POST
    * print response
    Then status 400

Scenario: cadastra usuario email vazio
    * def path = urlBase + cadastrarPath
    Given url path
    And header Content-Type = "application/json"
    And request { matricula: "201915020008", email: "", senha: "123456", nome: "Thauan Amorim", sexo: "MASCULINO", tipo: "COPED" }
    When method POST
    Then status 400
    * print response

Scenario: cadastra usuario email fora instituicao
    * def path = urlBase + cadastrarPath
    Given url path
    And header Content-Type = "application/json"
    And request { matricula: "201915020008", email: "thauan.amorim@gmail.com", senha: "123456", nome: "Thauan Amorim", sexo: "MASCULINO", tipo: "COPED" }
    When method POST
    Then status 400
    * print response

Scenario: cadastra usuario senha vazio
    * def path = urlBase + cadastrarPath
    Given url path
    And header Content-Type = "application/json"
    And request { matricula: "201915020008", email: "thauan.amorim@academico.ifpb.edu.br", senha: "", nome: "Thauan Amorim", sexo: "MASCULINO", tipo: "COPED" }
    When method POST
    Then status 400
    * print response

Scenario: cadastra um usuario nome vazio
    * def path = urlBase + cadastrarPath
    Given url path
    And header Content-Type = "application/json"
    And request { matricula: "201915020008", email: "thauan.amorim@academico.ifpb.edu.br", senha: "123456", nome: "", sexo: "MASCULINO", tipo: "COPED" }
    When method POST
    * print response
    Then status 400

Scenario: cadastra um usuario sexo vazio
    * def path = urlBase + cadastrarPath
    Given url path
    And header Content-Type = "application/json"
    And request { matricula: "201915020008", email: "thauan.amorim@academico.ifpb.edu.br", senha: "123456", nome: "Thauan Amorim", sexo: "", tipo: "COPED" }
    When method POST
    * print response
    Then status 400

Scenario: cadastra um usuario sexo qualquer outra coisa
    * def path = urlBase + cadastrarPath
    Given url path
    And header Content-Type = "application/json"
    And request { matricula: "201915020008", email: "thauan.amorim@academico.ifpb.edu.br", senha: "123456", nome: "Thauan Amorim", sexo: "teste", tipo: "COPED" }
    When method POST
    * print response
    Then status 400

Scenario: cadastra um usuario tipo vazio
    * def path = urlBase + cadastrarPath
    Given url path
    And header Content-Type = "application/json"
    And request { matricula: "201915020008", email: "thauan.amorim@academico.ifpb.edu.br", senha: "123456", nome: "Thauan Amorim", sexo: "MASCULINO", tipo: "" }
    When method POST
    * print response
    Then status 400

Scenario: cadastra um usuario tipo qualquer outra coisa
    * def path = urlBase + cadastrarPath
    Given url path
    And header Content-Type = "application/json"
    And request { matricula: "201915020008", email: "thauan.amorim@academico.ifpb.edu.br", senha: "123456", nome: "Thauan Amorim", sexo: "MASCULINO", tipo: "teste" }
    When method POST
    * print response
    Then status 400

Scenario: cadastra um usuario com sucesso
    * def path = urlBase + cadastrarPath
    Given url path
    And header Content-Type = "application/json"
    And request { matricula: "201915020008", email: "thauan.amorim.normal@academico.ifpb.edu.br", senha: "123456", nome: "Thauan Amorim normal", sexo: "MASCULINO", tipo: "COPED" }
    When method POST
    * print response
    Then status 200
