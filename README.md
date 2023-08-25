<h1 align="center"> SB Products </h1> <br>

![GitHub package.json version (subfolder of monorepo)](https://img.shields.io/github/v/tag/IgorPimentelG/sb-products)


Esse projeto foi desenvolvido como parte do estudo de revisão do Java e Spring Framework. O mesmo foi
implementado de forma a validar o funcionamento dos recursos do framework (controllers, segurança, 
services, gerenciamento de dados, validação, gerenciado de arquivos com upload e download, testes e entre 
outros), sem focar em funcionalidades específicas.

Foi utilizado diversas ferramentas do ecossistema do Spring, como o Spring Data, Spring Security e
Spring Web, mecanismos de validação, entre outros. Os dados são armazenados em um banco de dados gerenciado pelo 
MySQL e pelo FlawayDB para criação das migrações.

A estrutura do projeto é baseada na clean architecture, porém com algumas modificações na domian layer para evitar
duplicação.

Toda a API foi documentada com o uso do Swagger, uma ferramenta amplamente reconhecida para a criação de
documentação interativa e fácil de entender para APIs. O Swagger não apenas oferece uma representação 
visual dos endpoints, parâmetros e respostas da API, mas também permite que desenvolvedores e usuários 
possam explorar e testar os diversos recursos disponíveis.

Para validar o funcionamento da API, foram implementados testes utilizando o framework JUnit para testes de 
unidade e a biblioteca Rest Assured em conjunto com Test Containers para testes de integração. O Rest Assured 
permite enviar solicitações HTTP à API e verificar as respostas recebidas, simulando a interação real entre 
os componentes. Já os test containers criam ambientes isolados e temporários, como bancos de dados em contêineres 
docker, para garantir que os testes não afetem ambientes de desenvolvimento ou produção. Todos os testes são
avaliados pelo JaCoCo para definir a cobertura dos testes. Além dos testes, o SonarQube foi adicionado como 
forma de analisar continuamente a qualidade do código-fonte do projeto.

## Serviços Disponíveis

| Nome     | Métodos                             |
|----------|-------------------------------------|
| Auth     | `POST` `PUT`                        |
| User     | `POST` `PUT` `PATCH` `GET` `DELETE` |
| Products | `POST` `PUT` `GET` `DELETE`         |
| File     | `POST` `GET`                        |

## Requisitos
1. [Java 17 SDK](https://www.oracle.com/java/technologies/downloads/#java17)
2. [Maven](https://www.oracle.com/java/technologies/downloads/#java17)
3. [Docker](https://www.docker.com/)


## Rodando localmente

* É necessário informar as variáveis de ambiente no arquivo .env.

Clone o projeto

```bash
  git clone https://github.com/IgorPimentelG/sb-products.git
```

Entre no diretório do projeto

```bash
  cd sb-products
```

Execute o docker

```bash
  docker-compose up -d
```

O servidor executar na porta pardão 8080.

## Estrutura de arquivos

```
├───src
│   ├───main
│   │   ├───java
│   │   │   └───com
│   │   │       └───sb
│   │   │           └───products
│   │   │               ├───data
│   │   │               │   ├───errors
│   │   │               │   ├───gateway
│   │   │               │   │   ├───factories
│   │   │               │   │   └───outputs
│   │   │               │   └───usecases
│   │   │               │       ├───auth
│   │   │               │       ├───product
│   │   │               │       ├───storage
│   │   │               │       └───user
│   │   │               ├───domain
│   │   │               │   ├───entities
│   │   │               │   ├───errors
│   │   │               │   └───factories
│   │   │               ├───infra
│   │   │               │   ├───controller
│   │   │               │   │   ├───docs
│   │   │               │   │   │   ├───auth
│   │   │               │   │   │   ├───file
│   │   │               │   │   │   ├───product
│   │   │               │   │   │   └───user
│   │   │               │   │   └───dtos
│   │   │               │   ├───mapper
│   │   │               │   ├───repositories
│   │   │               │   └───services
│   │   │               └───main
│   │   │                   └───config
│   │   │                       ├───global
│   │   │                       ├───handles
│   │   │                       └───security
│   │   └───resources
│   │       └───db
│   │           └───migration


```

## Desenvolvedor

- [@IgorPimentelG](https://www.github.com/IgorPimentelG)


## Feedback

Se você tiver algum feedback, por favor me contactar por meio do e-mail: dev.igorpimentel@gmail.com