package com.example.RealtorVision.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageDetailsDto {
    private Long id;
    private String imageUrl;
    private boolean isDeleted;
    private LocalDate uploadDate;
    private LocalDate createdDate;
    private Long marketingOrderId;
    private List<String> hashtags;
}
