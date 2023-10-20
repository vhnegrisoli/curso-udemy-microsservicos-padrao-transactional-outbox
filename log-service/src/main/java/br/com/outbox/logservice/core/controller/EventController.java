package br.com.outbox.logservice.core.controller;

import br.com.outbox.logservice.core.document.Event;
import br.com.outbox.logservice.core.dto.LogFilters;
import br.com.outbox.logservice.core.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.stream.EventFilter;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/event")
public class EventController {

    private final EventService eventService;

    @GetMapping("{id}")
    public Event findById(@PathVariable String id) {
        return eventService.findById(id);
    }

    @GetMapping
    public List<Event> findAll(LogFilters filters) {
        return eventService.findAll(filters);
    }
}
