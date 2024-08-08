package org.example.advertisementservice.config;

import org.springframework.kafka.support.serializer.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
public class JsonSerializerWithJTM <T> extends JsonSerializer<T> { // This class extends the JsonSerializer provided by Spring Kafka, adding support for Java 8 date/time types

    public JsonSerializerWithJTM() { // Default constructor that registers the JavaTimeModule with the default ObjectMapper
        super();
        objectMapper.registerModule(new JavaTimeModule());
    }
    public JsonSerializerWithJTM(ObjectMapper objectMapper) { // Constructor that accepts an ObjectMapper and registers the JavaTimeModule with it
        super(objectMapper);
    }
}