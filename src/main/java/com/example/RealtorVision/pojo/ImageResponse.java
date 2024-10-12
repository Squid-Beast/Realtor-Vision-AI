package com.example.RealtorVision.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageResponse {
    private String status;
    private String message;

    public ImageResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
