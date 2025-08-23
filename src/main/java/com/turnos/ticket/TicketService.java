package com.turnos.ticket;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.turnos.api.dto.BoardItemDTO;
import com.turnos.api.dto.TicketResponseDTO;
import com.turnos.person.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TicketService {

  private final TicketRepository repo;

  private final SimpMessagingTemplate ws;

  private final AtomicInteger seq = new AtomicInteger(10);

  public TicketResponseDTO create(Person p) {
    char prefix = 'A';
    while (true) {
      String code = prefix + String.valueOf(seq.incrementAndGet());
      try {
        Ticket t = Ticket.builder()
            .code(code)
            .status(TicketStatus.CREATED)
            .createdAt(Instant.now())
            .person(p)
            .build();
        return buildTicketResponseDTO(repo.save(t));
      } catch (DataIntegrityViolationException ex) {
        // retry con otro code
      }
    }
  }

  @Transactional
  public TicketResponseDTO callNext(int moduleNumber) {
    Ticket t = repo.findFirstByStatusOrderByCreatedAtAsc(TicketStatus.CREATED)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay ticket pendientes"));
    t.setStatus(TicketStatus.CALLED);
    t.setCalledAt(Instant.now());
    t.setModuleNumber(moduleNumber);
    t = repo.save(t);
    BoardItemDTO dto = new BoardItemDTO(t.getCode(), t.getModuleNumber(), t.getPerson().getFirstName() + " " + t.getPerson().getLastName());
    ws.convertAndSend("/topic/board", dto);
    return buildTicketResponseDTO(t);
  }

  @Transactional
  public TicketResponseDTO serve(Long id) {
    Ticket t = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el ticket"));
    t.setStatus(TicketStatus.SERVED);
    t.setServedAt(Instant.now());
    Map<String, Object> payload = new HashMap<>();
    payload.put("action", "refresh");
    ws.convertAndSend("/topic/board", payload);
    return buildTicketResponseDTO(repo.save(t));
  }

  @Transactional
  public List<BoardItemDTO> lastCalled(int limit) {
    return repo.findTop10ByStatusOrderByCalledAtDesc(TicketStatus.CALLED).stream()
        .limit(limit)
        .map(t -> new BoardItemDTO(t.getCode(), t.getModuleNumber(), t.getPerson().getFirstName() + " " + t.getPerson().getLastName()))
        .toList();
  }

  private TicketResponseDTO buildTicketResponseDTO(final Ticket ticket) {
    return new TicketResponseDTO(ticket.getId(), ticket.getCode(), ticket.getStatus(), ticket.getCreatedAt(),
        ticket.getCreatedAt(), ticket.getServedAt(), ticket.getModuleNumber(),
        ticket.getPerson().getFirstName() + " " + ticket.getPerson().getLastName());
  }
}
