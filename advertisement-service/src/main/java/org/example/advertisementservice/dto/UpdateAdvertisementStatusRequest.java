package org.example.advertisementservice.dto;

import org.example.advertisementservice.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAdvertisementStatusRequest {

    private String status;
}