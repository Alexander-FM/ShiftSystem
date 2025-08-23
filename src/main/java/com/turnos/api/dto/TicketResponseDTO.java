package com.turnos.api.dto;

import java.time.Instant;

import com.turnos.ticket.TicketStatus;

public record TicketResponseDTO(Long id, String code, TicketStatus status, Instant createdAt, Instant calledAt, Instant servetAt,
                                Integer moduleNumber, String fullName) {

}
