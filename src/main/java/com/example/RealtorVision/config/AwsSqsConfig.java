package com.example.RealtorVision.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class AwsSqsConfig {

    @Value("${cloud.aws.region}")
    private String awsRegion;

    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder()
        .region(Region.of(awsRegion))
//        .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
        .build();
    }
}
