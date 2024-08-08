package org.example.advertisementservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AdvertisementNotFoundException extends RuntimeException{
    public AdvertisementNotFoundException(String s) {
        super(s);
    }
}