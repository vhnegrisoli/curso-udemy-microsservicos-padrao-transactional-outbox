package br.com.outbox.logservice.core.document;

import br.com.outbox.logservice.core.enums.EUserAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "event")
public class Event {

    @Id
    private String id;
    private User user;
    private EUserAction action;
    private String httpMethod;
    private String url;
    private LocalDateTime createdAt;
}
