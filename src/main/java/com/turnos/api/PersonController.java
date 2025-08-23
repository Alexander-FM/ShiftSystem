package com.turnos.api;

import java.util.Optional;

import com.turnos.api.dto.PersonResponseDTO;
import com.turnos.person.Person;
import com.turnos.person.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {

  private final PersonRepository persons;

  @GetMapping("/searchByDNI/{dni}")
  public PersonResponseDTO searchByDNI(@PathVariable(name = "dni") final String dni) {
    if (!dni.matches("\\d{8}")) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "DNI Inválido");
    }
    Optional<Person> p = persons.findByDni(dni);
    if (p.isPresent()) {
      return new PersonResponseDTO(p.get().getDni(), p.get().getFirstName(), p.get().getLastName());
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "DNI no encontrado");
    }
  }
}


