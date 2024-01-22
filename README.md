# 💻 Customer Service

Customer Service é um projeto fictício para cadastro de clientes com endereço, 
cuja busca pelo endereço a ser cadastrado usa a API externa VIA CEP (https://viacep.com.br/).<br/>
Assim, neste repositório temos uma API Rest que usa Spring, com Spring Boot,
Spring MVC, Spring Security e Spring Data JPA. Temos também disponíveis os
testes unitários e de integração usando JUnit e Mockito. <br/>A documentação da API
foi gerada usando Swagger e Spring Doc Open API.


### 🛠 Tecnologias

1. JDK 11
2. Maven 3
3. Spring Boot 2.5.6
4. H2 Database
5. Auth0 JWT

### ⚙️ Funcionalidades

- [x] Cadastro/atualização/exclusão de clientes;
- [x] Cadastro/atualização/exclusão de endereços dos clientes pelo CEP
  (um cliente pode ter múltiplos endereços) e os dados do endereço serão armazenados
conforme o retorno da API Externa VIA CEP;
- [x] Pesquisa de cliente com paginação e com os filtros Nome, E-mail, Gênero, UF e Localidade;
- [x] Tratamento de erros com respostas padronizadas;


### Instruções para uso da API

1. Clone o projeto deste repositório.
2. Execute a aplicação usando a classe ApiApplication usando sua IDE favorita.
3. Usando PostMan ou outro aplicativo, envie para o endpoint http://localhost:8080/login 
o seguinte corpo no formato JSON {"login":"giovanni","password":"123456"} e capture o TOKEN
de autorização.
4. Os demais endpoits tem acesso protegido, deve usar o TOKEN capturado
no passo anterior para usá-los, no PostMan use a opção "Bearer Token" na aba
"Authorization" da requisição e informe o TOKEN capturado no passo anterior antes
de executar a requisição.
5. Para os PostMan este repositório conta com o arquivo de requisições exportado
na versão 2.1, está disponível na pasta "resources" do projeto.
6. Para saber quais os endpoits disponíveis e testar sua execução, foi gerada
a documentação com Swagger e Spring Doc Open API, que poderá ser acessada em
http://localhost:8080/swagger-ui/index.html 
7. Para usar a interface do Swagger para executar requisições, deve capturar um
TOKEN no endpois "/login" (passo 3) e registrar no Swagger no botão do cadeado
do lado direito da interface inicial.
8. Poderá acessar o banco de dados pelo endereço http://localhost:8080/h2-console/ 
usando User Name: admin e o Password: admin
9. Esta API ainda não possuí Frontend implementados, logo as interações devem ocorrer
pelos endpoints, usando aplicativos de requisição REST ou pela documentação na
Swagger UI (passo 6).

## 📝 Licença

Este repositório é livre, estude bastante e seja feliz!<br/>
Giovanni Biffi<br/>
Tradin Ltda<br/>