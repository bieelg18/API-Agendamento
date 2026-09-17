package dev.bieelg18.Agendamento.dtos.agendamento;

import dev.bieelg18.Agendamento.dtos.servicos.ServicoAgendamentoDTO;
import dev.bieelg18.Agendamento.dtos.usuarios.ProfissionalAgendamentoDTO;
import dev.bieelg18.Agendamento.entities.Servicos;
import dev.bieelg18.Agendamento.entities.Usuario;
import dev.bieelg18.Agendamento.enums.StatusAgendamento;

import java.time.LocalDateTime;

public record ListarAgendamentoUsuarioDTO(
        Integer id,
        ProfissionalAgendamentoDTO profissional,
        ServicoAgendamentoDTO servico,
        LocalDateTime data,
        StatusAgendamento status
) {
}
