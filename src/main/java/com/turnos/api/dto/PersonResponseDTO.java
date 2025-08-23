package com.turnos.api.dto;

import jakarta.validation.constraints.Pattern;

public record PersonResponseDTO(
    @Pattern(regexp = "\\d{8}") String dni,
    String firstName,
    String lastName) {

}
