# Kafka MySQL to S3 Producer (SystemETL)

Este projeto Java lê dados da tabela `funcionarios` em um banco MySQL, converte para o formato Avro e envia para um tópico Kafka usando o Schema Registry.

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
