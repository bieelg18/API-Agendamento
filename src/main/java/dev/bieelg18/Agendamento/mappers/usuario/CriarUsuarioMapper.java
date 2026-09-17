package dev.bieelg18.Agendamento.mappers.usuario;

import dev.bieelg18.Agendamento.dtos.usuarios.CriarUsuarioDTO;
import dev.bieelg18.Agendamento.entities.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CriarUsuarioMapper {

    CriarUsuarioDTO toDTO(Usuario usuario);

    Usuario toEntity(CriarUsuarioDTO criarUsuarioDTO);

}
