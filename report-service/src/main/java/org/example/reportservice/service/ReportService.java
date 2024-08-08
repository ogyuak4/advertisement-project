package org.example.reportservice.service;

import org.example.reportservice.dto.UpdateReportRequest;
import org.example.reportservice.dto.converter.ReportDtoConverter;
import org.example.reportservice.exception.ReportNotFoundException;
import org.example.reportservice.dto.AdvertisementDto;
import org.example.reportservice.dto.ReportDto;
import org.example.reportservice.model.Report;
import org.example.reportservice.repository.ReportRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final ReportDtoConverter converter;

    public ReportService(ReportRepository reportRepository, ReportDtoConverter converter) {
        this.reportRepository = reportRepository;
        this.converter = converter;
    }

    @KafkaListener(
            topics = "advertisement-topic",
            groupId = "group-id",
            containerFactory = "advertisementDtoKafkaListenerContainerFactory"
    )
    public void consumeAdvertisementDto(AdvertisementDto advertisementDto) throws InterruptedException {
        Report report = new Report();
        report.setAdvertisementId(advertisementDto.getId());
        report.setUserName(advertisementDto.getUserName());
        report.setVisitCounter(advertisementDto.getVisitCounter());
        report.setReportText(advertisementDto.getTitle() + " isimli ilan " +
                advertisementDto.getUserName() + " kullanıcısı tarafından " +
                advertisementDto.getCreatedAt() + " tarihinde oluşturuldu ve " +
                advertisementDto.getVisitCounter() + " kere görüntülendi.");

        reportRepository.save(report);
    }

    public ReportDto updateReport(Long reportId, UpdateReportRequest updateReportRequest) {
        Report report = findReportById(reportId);
        Report updatedReport = new Report(
                report.getId(),
                report.getAdvertisementId(),
                updateReportRequest.getUserName(),
                updateReportRequest.getVisitCounter(),
                updateReportRequest.getReportText()
        );

        return converter.convert(reportRepository.save(updatedReport));
    }

    public Report findReportById(Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportNotFoundException("Report with id " + reportId + " couldn't found"));
    }
}