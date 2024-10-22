package com.example.RealtorVision.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class BedrockService {

    @Autowired
    private BedrockRuntimeClient bedrockClient;

    @Autowired
    private ObjectMapper objectMapper;

    public String generateTagsFromImage(byte[] imageBytes) {
        try {
            Map<String, Object> payload = createPayload();

            String jsonPayload = objectMapper.writeValueAsString(payload);
            InvokeModelRequest request = buildInvokeModelRequest(jsonPayload);

            InvokeModelResponse response = bedrockClient.invokeModel(request);
            return processResponse(response.body().asUtf8String());
        } catch (Exception e) {
            log.error("Error generating tags from image: {}", e.getMessage(), e);
            return "";
        }
    }

    private Map<String, Object> createPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("prompt", "Generate exactly 3 unique tags for the image related to house features. Return the tags as a comma-separated list with no additional text or formatting.");
        return payload;
    }

    private InvokeModelRequest buildInvokeModelRequest(String jsonPayload) {
        return InvokeModelRequest.builder()
                .modelId("us.meta.llama3-2-11b-instruct-v1:0")
                .accept("application/json")
                .contentType("application/json")
                .body(SdkBytes.fromUtf8String(jsonPayload))
                .build();
    }

    private String processResponse(String responseBody) {
        log.debug("Response Body: {}", responseBody);
        String generatedTags = extractTags(responseBody);
        return String.join(", ", processTags(generatedTags));
    }

    private String extractTags(String responseBody) {
        String tagsPattern = "(?i)tags[:\\s]*([^\\n]*)";
        Matcher matcher = Pattern.compile(tagsPattern).matcher(responseBody);

        if (matcher.find()) {
            String tags = matcher.group(1).trim();
            log.debug("Extracted Tags: {}", tags);
            return tags;
        }
        return "";
    }

    private Set<String> processTags(String tagsString) {
        Set<String> tagsSet = new LinkedHashSet<>();
        if (tagsString != null && !tagsString.isEmpty()) {
            String[] tagsArray = tagsString.split("\\s*,\\s*");
            for (String tag : tagsArray) {
                if (tagsSet.size() < 3) {
                    tagsSet.add(tag.trim());
                }
            }
        }
        return tagsSet;
    }
}
