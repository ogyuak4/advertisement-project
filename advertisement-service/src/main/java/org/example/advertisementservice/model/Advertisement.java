package org.example.advertisementservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "advertisements")
public class Advertisement {

    @Id
    private Long id;
    private String title;
    private BigDecimal price;
    private String description;
    private LocalDateTime createdAt;
    private String userName;
    private Boolean active; // true= active advertisement, false= passive advertisement

    @Field(targetType = FieldType.STRING)
    private Status status; // Storing enum as a string in MongoDB

    private Long visitCounter;
}
