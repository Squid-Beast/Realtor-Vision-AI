package com.example.RealtorVision.repository;

import com.example.RealtorVision.entity.MarketingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketingOrderRepository extends JpaRepository<MarketingOrder, Long> {
    
}
