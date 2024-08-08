package org.example.reportservice.dto.converter;

import org.example.reportservice.dto.ReportDto;
import org.example.reportservice.model.Report;
import org.springframework.stereotype.Component;

@Component
public class ReportDtoConverter {
    public ReportDto convert(Report from) {
        return new ReportDto(from.getId(),
                from.getAdvertisementId(),
                from.getUserName(),
                from.getVisitCounter(),
                from.getReportText()
        );
    }
}