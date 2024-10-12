package com.example.RealtorVision.repository;

import com.example.RealtorVision.entity.ListingDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingDetailsRepository extends JpaRepository<ListingDetails, Long> {
    
}
