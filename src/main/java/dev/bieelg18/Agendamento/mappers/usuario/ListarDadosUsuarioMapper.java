package dev.bieelg18.Agendamento.mappers.usuario;

import dev.bieelg18.Agendamento.dtos.usuarios.ListarDadosUsuarioDTO;
import dev.bieelg18.Agendamento.entities.Usuario;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ListarDadosUsuarioMapper {

    ListarDadosUsuarioDTO toDTO(Usuario usuario);

    Usuario toEntity(ListarDadosUsuarioDTO listarDadosUsuarioDTO);



}
