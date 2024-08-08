package org.example.reportservice.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementDto {

    private Long id;
    private String title;
    private BigDecimal price;
    private String description;
    private LocalDateTime createdAt;
    private String userName;
    private Boolean active;
    private Long visitCounter;

}