package com.turnos.api;

import java.util.List;

import com.turnos.api.dto.BoardItemDTO;
import com.turnos.api.dto.PersonResponseDTO;
import com.turnos.api.dto.TicketResponseDTO;
import com.turnos.person.Person;
import com.turnos.person.PersonRepository;
import com.turnos.ticket.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TicketController {

  private final TicketService service;

  private final PersonRepository persons;

  @PostMapping("/tickets")
  public TicketResponseDTO create(@Valid @RequestBody PersonResponseDTO dto) {
    Person p = persons.findByDni(dto.dni()).orElseGet(() -> {
      Person np = new Person();
      np.setDni(dto.dni());
      np.setFirstName(dto.firstName());
      np.setLastName(dto.lastName());
      return persons.save(np);
    });
    return service.create(p);
  }

  @GetMapping("/tickets/next")
  public TicketResponseDTO next(@RequestParam(name = "module") int module) {
    return service.callNext(module);
  }

  @PostMapping("/tickets/{id}/serve")
  public TicketResponseDTO serve(@PathVariable(name = "id") Long id) {
    return service.serve(id);
  }

  @GetMapping("/board/last")
  public List<BoardItemDTO> last(@RequestParam(name = "limit", defaultValue = "3") int limit) {
    return service.lastCalled(limit);
  }
}
