package br.com.outbox.userservice.core.repository;

import br.com.outbox.userservice.core.enums.EOutboxStatus;
import br.com.outbox.userservice.core.model.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxRepository extends JpaRepository<Outbox, Integer> {

    List<Outbox> findByStatusOrderByCreatedAt(EOutboxStatus status);

    int countByStatus(EOutboxStatus status);

    void deleteByStatus(EOutboxStatus status);
}
