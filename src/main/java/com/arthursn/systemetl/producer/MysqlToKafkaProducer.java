package com.arthursn.systemetl.producer;

import com.arthursn.systemetl.util.KafkaUtils;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import com.arthursn.systemetl.avro.Funcionario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class MysqlToKafkaProducer {

    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/mydb";
    private static final String MYSQL_USER = "thursns";
    private static final String MYSQL_PASSWORD = "abc123";
    private static final String KAFKA_TOPIC = "funcionarios-dados";
    private static final String KAFKA_BOOTSTRAP = System.getenv().getOrDefault("KAFKA_BOOTSTRAP", "localhost:9092");
    private static final String SCHEMA_REGISTRY_URL = System.getenv().getOrDefault("SCHEMA_REGISTRY_URL", "http://localhost:8081");

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BOOTSTRAP);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        props.put("schema.registry.url", SCHEMA_REGISTRY_URL);

        KafkaUtils.createTopicIfNotExists(KAFKA_TOPIC, KAFKA_BOOTSTRAP);

        try (Connection conn = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM funcionarios");
             KafkaProducer<String, Funcionario> producer = new KafkaProducer<>(props)) {

            while (rs.next()) {
                Funcionario funcionario = Funcionario.newBuilder()
                        .setId(rs.getInt("id"))
                        .setNome(rs.getString("nome"))
                        .setEmail(rs.getString("email"))
                        .setTelefone(rs.getString("telefone"))
                        .setCargo(rs.getString("cargo"))
                        .setDepartamento(rs.getString("departamento"))
                        .setSalario(rs.getBigDecimal("salario").doubleValue())
                        .setDataAdmissao(rs.getDate("data_admissao").toString())
                        .setCidade(rs.getString("cidade"))
                        .setSenioridade(rs.getString("senioridade"))
                        .build();

                ProducerRecord<String, Funcionario> record = new ProducerRecord<>(KAFKA_TOPIC, Integer.toString(funcionario.getId()), funcionario);
                producer.send(record, (metadata, exception) -> {
                    if (exception == null) {
                        System.out.printf("Enviado: %s%n", funcionario);
                    } else {
                        exception.printStackTrace();
                    }
                });
            }

            producer.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
