package dev.bieelg18.Agendamento.mappers.servicos;

import dev.bieelg18.Agendamento.dtos.servicos.ServicoAgendamentoDTO;
import dev.bieelg18.Agendamento.entities.Servicos;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServicoAgendamentoMapper {

    ServicoAgendamentoDTO toDTO(Servicos servicos);

}
