package br.com.outbox.logservice.core.repository;

import br.com.outbox.logservice.core.document.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventRepository extends MongoRepository<Event, String> {

    List<Event> findAllByOrderByCreatedAtDesc();
}
