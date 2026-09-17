package dev.bieelg18.Agendamento.mappers.servicos;

import dev.bieelg18.Agendamento.dtos.servicos.ListarServicoDTO;
import dev.bieelg18.Agendamento.entities.Servicos;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ListarServicoMapper {

    ListarServicoDTO toDTO(Servicos servicos);

    Servicos toEntity(ListarServicoDTO listarServicoDTO);

}
