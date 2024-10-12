package com.example.RealtorVision.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "marketing_order")
public class MarketingOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "package_name")
    private String packageName;
    @Column(name = "agent_id")
    private Long agentId;
    @Column(name = "coordinator_id")
    private Long coordinatorId;
    @ManyToOne
    @JoinColumn(name = "address_id",nullable = false)
    private Address address;
    @ManyToOne
    @JoinColumn(name = "listing_details_id",nullable = false)
    private ListingDetails listingDetails;
}
