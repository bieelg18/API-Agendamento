package dev.bieelg18.Agendamento.mappers.usuario;

import dev.bieelg18.Agendamento.dtos.usuarios.ProfissionalAgendamentoDTO;
import dev.bieelg18.Agendamento.entities.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfissionalAgendamentoMapper {

    ProfissionalAgendamentoDTO toDTO(Usuario usuario);

}
