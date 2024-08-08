package org.example.advertisementservice.service;

import org.example.advertisementservice.dto.AdvertisementDto;
import org.example.advertisementservice.dto.CreateAdvertisementRequest;
import org.example.advertisementservice.dto.UpdateAdvertisementRequest;
import org.example.advertisementservice.dto.UpdateAdvertisementStatusRequest;
import org.example.advertisementservice.dto.converter.AdvertisementDtoConverter;
import org.example.advertisementservice.exception.AdvertisementNotFoundException;
import org.example.advertisementservice.model.Advertisement;
import org.example.advertisementservice.model.Status;
import org.example.advertisementservice.repository.AdvertisementRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementDtoConverter converter;
    private final KafkaTemplate<String, AdvertisementDto> kafkaAdvertisementDtoTemplate;

    public AdvertisementService(AdvertisementRepository advertisementRepository, AdvertisementDtoConverter converter, KafkaTemplate<String, AdvertisementDto> kafkaAdvertisementDtoTemplate) {
        this.advertisementRepository = advertisementRepository;
        this.converter = converter;
        this.kafkaAdvertisementDtoTemplate = kafkaAdvertisementDtoTemplate;
    }

    public AdvertisementDto createAdvertisement(CreateAdvertisementRequest createAdvRequest) {

        Advertisement advertisement = new Advertisement();
        advertisement.setTitle(createAdvRequest.getTitle());
        advertisement.setPrice(createAdvRequest.getPrice());
        advertisement.setDescription(createAdvRequest.getDescription());
        advertisement.setUserName(createAdvRequest.getUserName());
        advertisement.setCreatedAt(LocalDateTime.now());
        advertisement.setActive(false);
        advertisement.setStatus(Status.PENDING);
        advertisement.setVisitCounter(Long.valueOf(0));

        return converter.convert(advertisementRepository.save(advertisement));
    }

    public AdvertisementDto updateAdvertisement(Long advertisementId, UpdateAdvertisementRequest updateAdvRequest) {
        Advertisement advertisement = findAdvertisementById(advertisementId);

        Advertisement updatedAdvertisement = new Advertisement(
                advertisement.getId(),
                updateAdvRequest.getTitle(),
                updateAdvRequest.getPrice(),
                updateAdvRequest.getDescription(),
                advertisement.getCreatedAt(),
                advertisement.getUserName(),
                false,
                Status.PENDING,// status
                advertisement.getVisitCounter());

        return converter.convert(advertisementRepository.save(updatedAdvertisement));
    }

    public AdvertisementDto updateAdvertisementStatus(Long advertisementId, UpdateAdvertisementStatusRequest updateAdvStatusRequest) {
        Advertisement advertisement = findAdvertisementById(advertisementId);

        Advertisement updatedAdvertisement = new Advertisement();
        updatedAdvertisement.setId(advertisement.getId());
        updatedAdvertisement.setTitle(advertisement.getTitle());
        updatedAdvertisement.setPrice(advertisement.getPrice());
        updatedAdvertisement.setDescription(advertisement.getDescription());
        updatedAdvertisement.setCreatedAt(advertisement.getCreatedAt());
        updatedAdvertisement.setUserName(advertisement.getUserName());
        updatedAdvertisement.setVisitCounter(advertisement.getVisitCounter());

        if(updateAdvStatusRequest.getStatus().equalsIgnoreCase("APPROVED")){
            updatedAdvertisement.setStatus(Status.APPROVED);
            updatedAdvertisement.setActive(true);

            // send message broker
            kafkaAdvertisementDtoTemplate.send("advertisement-topic", converter.convert(updatedAdvertisement));
        }
        else if(updateAdvStatusRequest.getStatus().equalsIgnoreCase("REJECTED")){
            updatedAdvertisement.setStatus(Status.REJECTED);
            updatedAdvertisement.setActive(false);
        }
        else{
            updatedAdvertisement.setStatus(Status.PENDING);
            updatedAdvertisement.setActive(false);
        }

        return converter.convert(advertisementRepository.save(updatedAdvertisement));
    }

    public Advertisement findAdvertisementById(Long advertisementId) {
        return advertisementRepository.findById(advertisementId)
                .orElseThrow(() -> new AdvertisementNotFoundException("Advertisement with this id " + advertisementId + " couldn't found"));
    }

    public List<AdvertisementDto> getAdvertisementsByStatus(String status) {
        return advertisementRepository.findByStatus(Status.valueOf(status))
                .stream()
                .map(converter::convert)
                .collect(Collectors.toList());
    }


    public List<AdvertisementDto> getPassiveAdvertisements() {
        return advertisementRepository.findByActiveFalse()
                .stream()
                .map(converter::convert)
                .collect(Collectors.toList());
    }
}