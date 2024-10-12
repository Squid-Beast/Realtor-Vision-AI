package com.example.RealtorVision.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "listing_details")
public class ListingDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "bathrooms")
    private int bathrooms;
    @Column(name = "bedrooms")
    private int bedrooms;
    @Column(name = "square_feet")
    private int squareFeet;
    @Column(name = "price")
    private double price;
}