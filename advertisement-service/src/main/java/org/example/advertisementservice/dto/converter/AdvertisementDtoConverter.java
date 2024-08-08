package org.example.advertisementservice.dto.converter;


import org.example.advertisementservice.dto.AdvertisementDto;
import org.example.advertisementservice.model.Advertisement;
import org.springframework.stereotype.Component;

@Component
public class AdvertisementDtoConverter {
    public AdvertisementDto convert(Advertisement from) {
        return new AdvertisementDto(from.getId(),
                from.getTitle(),
                from.getPrice(),
                from.getDescription(),
                from.getCreatedAt(),
                from.getUserName(),
                from.getActive(),
                from.getVisitCounter()
        );
    }
}