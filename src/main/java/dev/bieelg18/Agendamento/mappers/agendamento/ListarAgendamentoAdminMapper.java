package dev.bieelg18.Agendamento.mappers.agendamento;

import dev.bieelg18.Agendamento.dtos.agendamento.ListarAgendamentoAdminDTO;
import dev.bieelg18.Agendamento.entities.Agendamento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ListarAgendamentoAdminMapper {

    @Mapping(source = "cliente.id", target = "idCliente")
    @Mapping(source = "profissional.id", target = "idProfissional")
    @Mapping(source = "servico.id", target = "idServico")
    ListarAgendamentoAdminDTO toDTO(Agendamento agendamento);

    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "profissional", ignore = true)
    @Mapping(target = "servico", ignore = true)
    Agendamento toEntity(ListarAgendamentoAdminDTO listarAgendamentoAdminDTO);

}
