package com.example.RealtorVision.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MarketingOrderDetails {
    private Long id;
    private String packageName;
    private Long agentId;
    private Long coordinatorId;
    private Long addressId;
    private Long listingDetailsId;
}
