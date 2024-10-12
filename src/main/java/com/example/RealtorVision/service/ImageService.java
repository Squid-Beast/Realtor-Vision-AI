package com.example.RealtorVision.service;

import com.example.RealtorVision.config.AwsS3Config;
import com.example.RealtorVision.config.AwsSqsConfig;
import com.example.RealtorVision.entity.ImageDetails;
import com.example.RealtorVision.entity.MarketingOrder;
import com.example.RealtorVision.pojo.ImageDetailsDto;
import com.example.RealtorVision.pojo.ImageResponse;
import com.example.RealtorVision.pojo.MarketingOrderDetails;
import com.example.RealtorVision.repository.ImageDetailsRepository;
import com.example.RealtorVision.repository.MarketingOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ImageService {

    @Autowired
    private AwsSqsConfig sqsConfig;

    @Autowired
    private AwsS3Config awsS3Config;

    @Value("${cloud.aws.sqs.queue.url}")
    private String queueUrl;

    @Autowired
    private ImageDetailsRepository imageDetailsRepository;

    @Autowired
    private MarketingOrderRepository marketingOrderRepository;


    public ImageResponse processImageDetailsToQueue(ImageDetailsDto imageDetailsDto) {

        log.info("Received ImageDetailsDto for processing: {}", imageDetailsDto);

        if (imageDetailsDto.getImageUrl() == null || imageDetailsDto.getImageUrl().isEmpty()) {
            log.error("Image URL is null or empty for ImageDetailsDto: {}", imageDetailsDto);
            return new ImageResponse("Error.", "Image URL cannot be null or empty.");
        }

        CompletableFuture.runAsync(() -> {
            try {
                String objectKey = imageDetailsDto.getImageUrl();
                String updatedImageUrl = generatePublicUrl(objectKey);
                imageDetailsDto.setImageUrl(updatedImageUrl);
                saveImageDetails(imageDetailsDto);
                sendImageDetailsToSqs(imageDetailsDto);
                log.info("Image details sent successfully to {}", queueUrl);
            } catch (Exception e) {
                log.info("Error processing image details.");
            }
        });
        return new ImageResponse("Success.", "Tags will be generated and available in a few seconds. Please refresh the window.");
    }

    private String generatePublicUrl(String objectKey) {
        String bucketName = "listing-images-realtor-vision";
        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, objectKey);
    }

    private void saveImageDetails(ImageDetailsDto imageDetailsDto) {
        ImageDetails imageDetails = new ImageDetails();
        imageDetails.setId(imageDetailsDto.getId());
        imageDetails.setImageUrl(imageDetailsDto.getImageUrl());
        imageDetails.setDeleted(imageDetailsDto.isDeleted());
        imageDetails.setCreatedDate(imageDetailsDto.getCreatedDate());
        imageDetails.setUploadDate(imageDetailsDto.getUploadDate());
        imageDetails.setHashtags(imageDetailsDto.getHashtags());

        Long marketingOrderId = imageDetailsDto.getMarketingOrderId();
        if (marketingOrderId != null) {
            MarketingOrder marketingOrder = marketingOrderRepository.findById(marketingOrderId)
                    .orElseThrow(() -> new RuntimeException("Marketing Order not found"));
            imageDetails.setMarketingOrder(marketingOrder);
        }

        imageDetailsRepository.save(imageDetails);
    }

    private void sendImageDetailsToSqs(ImageDetailsDto imageDetailsDto) {
        SqsClient sqsClient = sqsConfig.sqsClient();
        String messageBody = String.format("Image Details: %s", imageDetailsDto.toString());

        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .build();

        sqsClient.sendMessage(sendMessageRequest);
        log.info("Image details sent to SQS queue: {}", imageDetailsDto);
    }
}
