package dev.bieelg18.Agendamento.dtos.servicos;

import java.math.BigDecimal;

public record CriarServicoDTO(
        String servico,
        BigDecimal preco,
        String duracao
) {
}
