package com.example.consumer.kafkaListener;

import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@EnableKafka
@AllArgsConstructor
public class Listener {

    @KafkaListener(topics = "${spring.kafka.topic}", groupId = "groupId")
    public void listen(String message) {
        System.out.println(message);
    }
}
