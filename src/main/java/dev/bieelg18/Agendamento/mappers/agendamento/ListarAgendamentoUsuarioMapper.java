package dev.bieelg18.Agendamento.mappers.agendamento;

import dev.bieelg18.Agendamento.dtos.agendamento.ListarAgendamentoUsuarioDTO;
import dev.bieelg18.Agendamento.entities.Agendamento;
import dev.bieelg18.Agendamento.mappers.servicos.ServicoAgendamentoMapper;
import dev.bieelg18.Agendamento.mappers.usuario.ProfissionalAgendamentoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
uses = {ProfissionalAgendamentoMapper.class, ServicoAgendamentoMapper.class})
public interface ListarAgendamentoUsuarioMapper {

    ListarAgendamentoUsuarioDTO toDTO(Agendamento agendamento);

    Agendamento toEntity(ListarAgendamentoUsuarioDTO listarAgendamentoUsuarioDTO);

}
