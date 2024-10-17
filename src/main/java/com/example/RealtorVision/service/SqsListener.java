package com.example.RealtorVision.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SqsListener{

    @Autowired
    private DescriptionService descriptionService;

    @JmsListener(destination="image-tags-generation-queue")
    public void receiveMessage(String imageUrl) {
        log.info("Received message: {}", imageUrl);
    }

}