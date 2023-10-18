package br.com.outbox.logservice.core.document;

import br.com.outbox.logservice.core.enums.EUserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Integer id;
    private String username;
    private String fullName;
    private String document;
    private String email;
    private LocalDate birthday;
    private EUserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
