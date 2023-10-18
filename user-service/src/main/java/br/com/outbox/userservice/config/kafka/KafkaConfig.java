package br.com.outbox.userservice.config.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

import static br.com.outbox.userservice.core.enums.EUserAction.*;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private static final Integer PARTITION_COUNT = 1;
    private static final Integer REPLICA_COUNT = 1;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.producer.max-block-ms}")
    private String maxBlockMs;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerProps());
    }

    private Map<String, Object> producerProps() {
        var props = new HashMap<String, Object>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, maxBlockMs);
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, maxBlockMs);
        props.put(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, maxBlockMs);
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, maxBlockMs);
        props.put(ProducerConfig.SOCKET_CONNECTION_SETUP_TIMEOUT_MAX_MS_CONFIG, maxBlockMs);
        return props;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    private NewTopic buildTopic(String name) {
        return TopicBuilder
            .name(name)
            .partitions(PARTITION_COUNT)
            .replicas(REPLICA_COUNT)
            .build();
    }

    @Bean
    public NewTopic logCreateUserTopic() {
        return buildTopic(CREATE_USER.getTopic());
    }

    @Bean
    public NewTopic logUpdateUserTopic() {
        return buildTopic(UPDATE_USER.getTopic());
    }

    @Bean
    public NewTopic logDeleteUserTopic() {
        return buildTopic(DELETE_USER.getTopic());
    }
}