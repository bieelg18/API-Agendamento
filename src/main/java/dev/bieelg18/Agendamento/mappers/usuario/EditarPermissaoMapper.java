package dev.bieelg18.Agendamento.mappers.usuario;

import dev.bieelg18.Agendamento.dtos.usuarios.EditarPermissaoDTO;
import dev.bieelg18.Agendamento.entities.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EditarPermissaoMapper {

    EditarPermissaoDTO toDTO(Usuario usuario);

    Usuario toEntity(EditarPermissaoDTO editarPermissaoDTO);

}
