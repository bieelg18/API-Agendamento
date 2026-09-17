package dev.bieelg18.Agendamento.mappers.servicos;

import dev.bieelg18.Agendamento.dtos.servicos.CriarServicoDTO;
import dev.bieelg18.Agendamento.entities.Servicos;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CriarServicoMapper {

    CriarServicoDTO toDTO(Servicos servicos);

    Servicos toEntity(CriarServicoDTO criarServicoDTO);

}
