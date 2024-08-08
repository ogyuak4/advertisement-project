package org.example.advertisementservice.controller;

import org.example.advertisementservice.dto.AdvertisementDto;
import org.example.advertisementservice.dto.CreateAdvertisementRequest;
import org.example.advertisementservice.dto.UpdateAdvertisementRequest;
import org.example.advertisementservice.dto.UpdateAdvertisementStatusRequest;
import org.example.advertisementservice.service.AdvertisementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/advertisement")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    public AdvertisementController(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    @PostMapping
    public ResponseEntity<AdvertisementDto> createAdvertisement (@RequestBody CreateAdvertisementRequest createAdvRequest){
        return ResponseEntity.ok(advertisementService.createAdvertisement(createAdvRequest));
    }

    @PutMapping("/{advertisementId}")
    public ResponseEntity<AdvertisementDto> updateAdvertisement (@PathVariable Long advertisementId, @RequestBody UpdateAdvertisementRequest updateAdvRequest) {
        return ResponseEntity.ok(advertisementService.updateAdvertisement(advertisementId, updateAdvRequest));
    }

    // Only Admin can use it
    @GetMapping("/passive")
    public ResponseEntity<List<AdvertisementDto>> getPassiveAdvertisements() {
        return ResponseEntity.ok(advertisementService.getPassiveAdvertisements());
    }

    // Only Admin can use it
    @GetMapping("/{status}")
    public ResponseEntity<List<AdvertisementDto>> getAdvertisementsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(advertisementService.getAdvertisementsByStatus(status));
    }

    // Only Admin can use it
    @PutMapping("/status/{advertisementId}")
    public ResponseEntity<AdvertisementDto> updateAdvertisementStatus (@PathVariable Long advertisementId, @RequestBody UpdateAdvertisementStatusRequest updateAdvStatusRequest) {
        return ResponseEntity.ok(advertisementService.updateAdvertisementStatus(advertisementId, updateAdvStatusRequest));
    }

}