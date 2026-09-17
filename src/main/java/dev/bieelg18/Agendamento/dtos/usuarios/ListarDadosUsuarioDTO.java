package dev.bieelg18.Agendamento.dtos.usuarios;

public record ListarDadosUsuarioDTO(
        Integer id,
        String nome,
        String email,
        String permissao
) {
}
