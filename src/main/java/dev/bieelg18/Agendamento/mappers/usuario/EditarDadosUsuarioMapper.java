package dev.bieelg18.Agendamento.mappers.usuario;

import dev.bieelg18.Agendamento.dtos.usuarios.EditarDadosUsuarioDTO;
import dev.bieelg18.Agendamento.entities.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EditarDadosUsuarioMapper {

    EditarDadosUsuarioDTO toDTO(Usuario usuario);

    Usuario toEntity(EditarDadosUsuarioDTO editarDadosUsuarioDTO);

}
