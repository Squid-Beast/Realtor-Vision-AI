package com.example.RealtorVision.repository;

import com.example.RealtorVision.entity.ImageDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageDetailsRepository extends JpaRepository<ImageDetails,Long>{
    
}
