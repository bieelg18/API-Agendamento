package dev.bieelg18.Agendamento.mappers.servicos;

import dev.bieelg18.Agendamento.dtos.servicos.EditarServicoDTO;
import dev.bieelg18.Agendamento.entities.Servicos;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EditarServicoMapper {

    EditarServicoDTO toDTO(Servicos servicos);

    Servicos toEntity(EditarServicoDTO editarServicoDTO);

}
