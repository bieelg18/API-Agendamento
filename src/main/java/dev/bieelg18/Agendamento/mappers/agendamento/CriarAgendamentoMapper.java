package dev.bieelg18.Agendamento.mappers.agendamento;

import dev.bieelg18.Agendamento.dtos.agendamento.CriarAgendamentoDTO;
import dev.bieelg18.Agendamento.entities.Agendamento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CriarAgendamentoMapper {

    @Mapping(source = "profissional.id", target = "idProfissional")
    @Mapping(source = "servico.id", target = "idServico")
    CriarAgendamentoDTO toDTO(Agendamento agendamento);

    @Mapping(target = "profissional", ignore = true)
    @Mapping(target = "servico", ignore = true)
    Agendamento toEntity(CriarAgendamentoDTO criarAgendamentoDTO);

}
