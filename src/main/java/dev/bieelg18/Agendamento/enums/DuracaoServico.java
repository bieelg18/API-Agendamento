package dev.bieelg18.Agendamento.enums;

import lombok.*;

@RequiredArgsConstructor
@Getter
public enum DuracaoServico {
    VINTE_MINUTOS(20),
    TRINTA_MINUTOS(30),
    SESSENTA_MINUTOS(60);

    private final int minutos;
}


