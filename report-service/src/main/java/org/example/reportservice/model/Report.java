package org.example.reportservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reports") // Specify the MongoDB collection name
public class Report {

    @Id
    private Long id;
    private Long advertisementId;
    private String userName;
    private Long visitCounter;
    private String reportText;
}
