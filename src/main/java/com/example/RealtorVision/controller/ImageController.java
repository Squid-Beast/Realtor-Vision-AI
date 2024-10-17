package com.example.RealtorVision.controller;

import com.example.RealtorVision.pojo.ImageDetailsDto;
import com.example.RealtorVision.pojo.ImageResponse;
import com.example.RealtorVision.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/image")
public class ImageController{

    @Autowired
    private ImageService imageService;;

    @PostMapping("/bytes")
    public ResponseEntity<ImageResponse> generateHashtags(@RequestBody ImageDetailsDto imageDetailsList){
        ImageResponse response = imageService.processImageDetailsToQueue(imageDetailsList);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}