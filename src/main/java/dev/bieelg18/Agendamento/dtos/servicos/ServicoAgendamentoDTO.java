package dev.bieelg18.Agendamento.dtos.servicos;

import dev.bieelg18.Agendamento.enums.DuracaoServico;
import dev.bieelg18.Agendamento.enums.TipoServico;

import java.math.BigDecimal;

public record ServicoAgendamentoDTO(
        Integer id,
        TipoServico servico,
        BigDecimal preco,
        DuracaoServico duracao
) {
}
