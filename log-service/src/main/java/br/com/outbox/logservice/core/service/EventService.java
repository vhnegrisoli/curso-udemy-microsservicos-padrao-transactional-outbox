package br.com.outbox.logservice.core.service;

import br.com.outbox.logservice.config.exception.ValidationException;
import br.com.outbox.logservice.core.document.Event;
import br.com.outbox.logservice.core.repository.EventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public void saveEvent(Event event) {
        eventRepository.save(event);
    }

    public Event findById(String id) {
        return eventRepository
            .findById(id)
            .orElseThrow(() -> new ValidationException("Event not found by ID!"));
    }

    public List<Event> findAll() {
        return eventRepository.findAllByOrderByCreatedAtDesc();
    }
}
