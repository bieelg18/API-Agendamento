package dev.bieelg18.Agendamento.dtos.agendamento;

import dev.bieelg18.Agendamento.entities.Servicos;

import java.time.LocalDateTime;

public record CriarAgendamentoDTO(
        Integer idProfissional,
        Integer idServico,
        LocalDateTime data
){

}
