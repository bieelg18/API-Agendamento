package dev.bieelg18.Agendamento.mappers.agendamento;

import dev.bieelg18.Agendamento.dtos.agendamento.EditarStatusDTO;
import dev.bieelg18.Agendamento.entities.Agendamento;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EditarStatusMapper {

    EditarStatusDTO toDTO(Agendamento agendamento);

    Agendamento toEntity(EditarStatusDTO editarStatusDTO);

}
