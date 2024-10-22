package com.example.RealtorVision.service;

import com.example.RealtorVision.config.AwsS3Config;
import com.example.RealtorVision.config.AwsSqsConfig;
import com.example.RealtorVision.entity.ImageDetails;
import com.example.RealtorVision.entity.MarketingOrder;
import com.example.RealtorVision.pojo.ImageDetailsDto;
import com.example.RealtorVision.pojo.ImageResponse;
import com.example.RealtorVision.repository.ImageDetailsRepository;
import com.example.RealtorVision.repository.MarketingOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ImageService {

    @Autowired
    private AwsSqsConfig sqsConfig;

    @Autowired
    private AwsS3Config awsS3Config;

    @Value("${cloud.aws.sqs.queue.url}")
    private String queueUrl;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Autowired
    private ImageDetailsRepository imageDetailsRepository;

    @Autowired
    private MarketingOrderRepository marketingOrderRepository;

    @Autowired
    private S3Client s3Client;

    @Autowired
    private BedrockService bedrockService;

    public ImageResponse processImageDetailsToQueue(ImageDetailsDto imageDetailsDto) {
        List<S3Object> objectList = listS3Objects();
        log.info("Total Objects in Bucket: {}", objectList.size());

        objectList.forEach(s3Object -> processS3Object(s3Object, imageDetailsDto));

        return new ImageResponse("Success", "Tags are being generated. Refresh the page in 2-3 seconds.");
    }

    private List<S3Object> listS3Objects() {
        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();
        ListObjectsV2Response listObjectsResponse = s3Client.listObjectsV2(listObjectsV2Request);
        return listObjectsResponse.contents();
    }

    private void processS3Object(S3Object s3Object, ImageDetailsDto imageDetailsDto) {
        String objectKey = s3Object.key();
        log.info("Processing S3 Object with Key: {}", objectKey);

        byte[] imageBytes = getObjectBytes(objectKey);
        if (imageBytes == null) {
            log.error("Failed to retrieve bytes for S3 Object with Key: {}", objectKey);
            return;
        }

        try {
            String tags = bedrockService.generateTagsFromImage(imageBytes);

            ImageDetails imageDetails = new ImageDetails();
            imageDetails.setHashtags(tags);
            String presignedUrl = generatePresignedUrl(objectKey);
            imageDetails.setImageUrl(presignedUrl);
            imageDetails.setUploadDate(LocalDate.now());
            imageDetails.setCreatedDate(LocalDate.now());
            Long marketingOrderId = imageDetailsDto.getMarketingOrderId();
            Optional<MarketingOrder> optionalOrder = marketingOrderRepository.findById(marketingOrderId);

            if (optionalOrder.isPresent()) {
                imageDetails.setMarketingOrder(optionalOrder.get());
            } else {
                throw new EntityNotFoundException("MarketingOrder not found for ID: " + marketingOrderId);
            }

            imageDetailsRepository.save(imageDetails);

            log.info("Generated Tags for S3 Object {}: {}", objectKey, tags);
        } catch (Exception e) {
            log.error("Error generating tags for image: {}", objectKey, e);
        }
    }

    private byte[] getObjectBytes(String objectKey) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();
            ResponseBytes<GetObjectResponse> s3ObjectBytes = s3Client.getObjectAsBytes(getObjectRequest);
            return s3ObjectBytes.asByteArray();
        } catch (S3Exception e) {
            log.error("Error retrieving object bytes for key: {}", objectKey, e);
            return null;
        }
    }
    private String generatePresignedUrl(String objectKey) {
        try {
            Duration expiration = Duration.ofHours(12);
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            return awsS3Config.s3Presigner().presignGetObject(presignRequest -> presignRequest
                    .getObjectRequest(getObjectRequest)
                    .signatureDuration(expiration)).url().toString();
        } catch (Exception e) {
            log.error("Error generating presigned URL for object key: {}", objectKey, e);
            return null;
        }
    }

}
