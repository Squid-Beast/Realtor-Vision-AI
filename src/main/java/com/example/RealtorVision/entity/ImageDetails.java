package com.example.RealtorVision.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "image_details")
public class ImageDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "is_deleted")
    private boolean isDeleted;
    @Column(name = "upload_date")
    private LocalDate uploadDate = LocalDate.now();
    @Column(name = "created_date")
    private LocalDate createdDate = LocalDate.now();
    @ManyToOne
    @JoinColumn(name = "marketing_order_id",nullable = false)
    private MarketingOrder marketingOrder;
    @Column(name = "hashtags")
    private List<String> hashtags;
}