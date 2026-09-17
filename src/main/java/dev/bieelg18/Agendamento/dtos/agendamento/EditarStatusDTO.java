package dev.bieelg18.Agendamento.dtos.agendamento;

import dev.bieelg18.Agendamento.enums.StatusAgendamento;

public record EditarStatusDTO(
        StatusAgendamento status
) {
}
