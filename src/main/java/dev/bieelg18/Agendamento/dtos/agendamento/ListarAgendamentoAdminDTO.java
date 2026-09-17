package dev.bieelg18.Agendamento.dtos.agendamento;

import dev.bieelg18.Agendamento.enums.StatusAgendamento;

import java.time.LocalDateTime;

public record ListarAgendamentoAdminDTO(
        Integer id,
        Integer idCliente,
        Integer idProfissional,
        Integer idServico,
        LocalDateTime data,
        StatusAgendamento status
) {
}
