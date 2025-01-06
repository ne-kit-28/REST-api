package com.example.demo.service;

public interface KafkaSenderService {
    void sendAudit(String message);
}
