package org.example.reportservice.repository;

import org.example.reportservice.model.Report;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportRepository extends MongoRepository<Report, Long> {
}
