package org.example.reportservice.controller;

import org.example.reportservice.dto.ReportDto;
import org.example.reportservice.dto.UpdateReportRequest;
import org.example.reportservice.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PutMapping("/{reportId}")
    public ResponseEntity<ReportDto> updateReport (@PathVariable Long reportId, @RequestBody UpdateReportRequest updateReportRequest) {
        return ResponseEntity.ok(reportService.updateReport(reportId, updateReportRequest));
    }
}