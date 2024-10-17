package com.example.RealtorVision.service;

import com.example.RealtorVision.config.AwsS3Config;
import com.example.RealtorVision.config.AwsSqsConfig;
import com.example.RealtorVision.repository.ImageDetailsRepository;
import com.example.RealtorVision.repository.MarketingOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DescriptionService {
    @Autowired
    private ImageDetailsRepository imageDetailsRepository;
    @Autowired
    private MarketingOrderRepository marketingOrderRepository;
    @Autowired
    private AwsS3Config awsS3Config;
    @Autowired
    private AwsSqsConfig awsSqsConfig;

}
