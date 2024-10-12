package com.example.RealtorVision.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListingDetailsDto {
    private Long id;
    private int bathrooms;
    private int bedrooms;
    private int squareFeet;
    private double price;
}
