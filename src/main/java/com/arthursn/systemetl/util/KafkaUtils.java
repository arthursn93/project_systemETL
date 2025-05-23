package com.arthursn.systemetl.util;

import org.apache.kafka.clients.admin.*;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaUtils {

    public static void createTopicIfNotExists(String topicName, String bootstrapServers) {
        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        try (AdminClient admin = AdminClient.create(config)) {
            boolean exists = admin.listTopics().names().get().contains(topicName);
            if (!exists) {
                NewTopic newTopic = new NewTopic(topicName, 1, (short) 1); // 1 partição, 1 replicação
                admin.createTopics(Collections.singletonList(newTopic)).all().get();
                System.out.println("Tópico criado: " + topicName);
            } else {
                System.out.println("Tópico já existe: " + topicName);
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Erro ao verificar/criar tópico: " + e.getMessage());
        }
    }
}
