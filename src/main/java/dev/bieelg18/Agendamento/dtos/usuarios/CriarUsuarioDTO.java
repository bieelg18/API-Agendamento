package dev.bieelg18.Agendamento.dtos.usuarios;

public record CriarUsuarioDTO(
        String nome,
        String email,
        String senha,
        String permissao
) {
}
