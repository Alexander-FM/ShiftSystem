package com.turnos.api;

import com.turnos.api.dto.PersonResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/reniec")
public class ReniecController {
    @PostMapping("/lookup/{dni}")
    public PersonResponseDTO lookup(@PathVariable(name = "dni") String dni){
        if (!dni.matches("\\d{8}"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "DNI inválido");
        // MOCK - reemplazar con integración real a RENIEC
        return new PersonResponseDTO(dni, "Juan", "Pérez");
    }
}
