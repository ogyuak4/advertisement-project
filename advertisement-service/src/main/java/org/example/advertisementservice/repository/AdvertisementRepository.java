package org.example.advertisementservice.repository;

import org.example.advertisementservice.model.Advertisement;
import org.example.advertisementservice.model.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface AdvertisementRepository extends MongoRepository<Advertisement, Long> {

    // Find advertisements by status
    List<Advertisement> findByStatus(Status status);

    // Find inactive advertisements (active is false)
    List<Advertisement> findByActiveFalse();

    // Find the last 10 approved advertisements, sorted by creation time in descending order
    @Query("{ 'status': 'APPROVED' }")
    List<Advertisement> findTop10ByStatusOrderByCreatedAtDesc();
}
