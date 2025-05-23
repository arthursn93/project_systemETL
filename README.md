# Sobre o projeto
Este projeto é uma pequena aplicação Java que conecta um banco de dados MySQL a um sistema Kafka, que é usado para distribuir dados em tempo real para outras aplicações. A comunicação entre sistemas usa um formato chamado Avro, que é um jeito eficiente e organizado de estruturar dados.

# Etapas do desenvolvimento
## 1.Definir o formato dos dados com Avro:
Criamos um "modelo" chamado Funcionario.avsc que descreve quais informações de um funcionário vamos enviar: id, nome, email, cargo, salário etc. Esse arquivo é usado para gerar uma classe Java.

## 2.Ler dados do MySQL:
O programa se conecta ao banco MySQL, faz uma consulta na tabela de funcionários e traz todos os dados.

## 3.Converter os dados para Avro:
Para cada funcionário, criamos um objeto do tipo Funcionario (gerado automaticamente a partir do arquivo Avro). Isso garante que os dados tenham sempre o formato esperado.

## 4.Enviar dados para Kafka:
Usamos o Kafka para enviar os dados de forma rápida e confiável para outros sistemas. Para isso, configuramos um "produtor" Kafka que sabe enviar mensagens no formato Avro.

## 5.Gerenciar tópicos Kafka:
Antes de enviar, o programa verifica se o tópico Kafka (como uma "caixa postal") existe. Se não existir, cria automaticamente.

## 6.Ambiente de execução com Docker:
Criamos um ambiente Docker com Zookeeper, Kafka e o nosso produtor Java. Isso facilita rodar tudo localmente sem instalar nada manualmente.

---

## Tecnologias usadas

- Java 17
- Apache Kafka + Confluent Schema Registry
- Apache Avro (para serialização de dados)
- MySQL (banco de dados)
- Docker & Docker Compose (para rodar Kafka, Zookeeper e produtor)
- AWS SDK para futura integração com S3 (não implementado ainda)

---

## Como usar

### Pré-requisitos

- Docker e Docker Compose instalados
- Banco MySQL rodando com a tabela `funcionarios`
- (Opcional) Schema Registry rodando localmente ou via Docker

### Passos para rodar localmente

1. **Build do projeto**  
   ```bash
   mvn clean package
   mvn generate-sources


