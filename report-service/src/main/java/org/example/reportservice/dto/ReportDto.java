package org.example.reportservice.dto;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {

    private Long id;
    private Long advertisementId;
    private String userName;
    private Long visitCounter;
    private String reportText;
}