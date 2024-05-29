## Desafio de Integração de Sistemas - vertical logistica

O desafio consiste em criar um sistema que integre dois sistemas, processando um arquivo de pedidos desnormalizado de um sistema legado para um formato JSON normalizado. Este documento fornece detalhes sobre a solução proposta, incluindo a descrição do problema, a estrutura da aplicação, os endpoints da API e instruções para execução e teste.

### Descrição do Problema

Temos um sistema legado que armazena informações de pedidos em um arquivo desnormalizado. Cada linha deste arquivo representa uma parte de um pedido, e os campos são padronizados por tamanho. O objetivo é receber este arquivo via API REST, processá-lo e retorná-lo em formato JSON normalizado.

### Solução Proposta

A solução proposta consiste em uma aplicação Spring Boot que oferece endpoints REST para receber o arquivo de pedidos, processá-lo e retornar os dados normalizados. O sistema utiliza o banco de dados H2 para armazenar os pedidos processados temporariamente durante o processamento.

### Endpoints da API

#### 1. Entrada de Dados

- **URL:** `/api/orders`
- **Método:** `POST`
- **Corpo da Requisição:** O corpo da requisição deve conter o conteúdo do arquivo de pedidos em formato de string.
- **Resposta de Sucesso:** Retorna uma mensagem indicando que os pedidos foram processados com sucesso.
    - Código: `201 Created`
    - Corpo:
      ```json
      {
        "message": "Pedidos processados com sucesso."
      }
      ```
- **Resposta de Erro:** Retorna uma mensagem de erro em caso de falha no processamento.
    - Código: `400 Bad Request`
    - Corpo:
      ```json
      {
        "message": "Erro ao processar pedido."
      }
      ```

### Executando a Aplicação

1. Clone o repositório:

   ```bash
   git clone https://github.com/Rielson-Leandro/vertical-logistica
   ```

2. Navegue até o diretório do projeto:

   ```bash
   cd vertical-logistica
   ```

3. Execute a aplicação Spring Boot:

   ```bash
    docker-compose up --build
   ```

### Testando a API

Você pode usar o software [Insomnia](https://insomnia.rest/download) para testar os endpoints da API. Baixe a coleção de requisições Insomnia [aqui](https://github.com/Rielson-Leandro/vertical-logistica/tree/main/collection).

### Link para Coleção Insomnia

[Coleção Insomnia](https://github.com/Rielson-Leandro/vertical-logistica/tree/main/collection)

---
