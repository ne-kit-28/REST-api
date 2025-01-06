package com.example.demo.service.serviceImpl;

import com.example.demo.service.KafkaSenderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSenderServiceImpl implements KafkaSenderService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    //@Value("${spring.kafka.producer.topic}")
    private String topic = "REST-api";

    public KafkaSenderServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendAudit(String message) {
        kafkaTemplate.send(topic, message);
    }
}
