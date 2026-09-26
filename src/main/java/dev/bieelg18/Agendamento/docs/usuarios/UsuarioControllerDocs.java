package dev.bieelg18.Agendamento.docs.usuarios;

import dev.bieelg18.Agendamento.dtos.usuarios.CriarUsuarioDTO;
import dev.bieelg18.Agendamento.dtos.usuarios.EditarDadosUsuarioDTO;
import dev.bieelg18.Agendamento.dtos.usuarios.EditarPermissaoDTO;
import dev.bieelg18.Agendamento.dtos.usuarios.ListarDadosUsuarioDTO;
import dev.bieelg18.Agendamento.enums.Permissao;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UsuarioControllerDocs {

    @Operation(summary = "Cria um novo usuário no banco de dados",
    description = "Não é necessário estar autenticado para acessar esse método")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado"),
            @ApiResponse(responseCode = "400", description = "Usuário não criado")
    })
    ListarDadosUsuarioDTO criarUsuario(CriarUsuarioDTO criarUsuarioDTO);

    @Operation(summary = "Lista todos os usuário existentes no banco de dados",
    description = "É necessário estar autenticado e possuir permissão ADMIN para acessar este método")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista com todos os usuários"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    List<ListarDadosUsuarioDTO> todosOsUsuarios();

    @Operation(summary = "Busca um usuário no banco de dados através do e-mail fornecido",
    description = "É necessário estar autenticado e possuir permissão ADMIN para acessar este método")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    ListarDadosUsuarioDTO buscarEmail(String email);

    @Operation(summary = "Edita a permissão de um usuário existente no banco de dados",
    description = "É necessário estar autenticado e possuir permissão ADMIN para acessar este método")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permissão alterada"),
            @ApiResponse(responseCode = "404", description = "Permissão não alterada, usuário não encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    ListarDadosUsuarioDTO permissao(Integer id, EditarPermissaoDTO permissaoDTO);

    @Operation(summary = "Altera dados de cadastro do usuário que chamou a requisição",
    description = "É necessário estar autenticado para acessar esse método")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados alterados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Dados não alterados"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    ListarDadosUsuarioDTO editarDados(EditarDadosUsuarioDTO editarDTO, Authentication authentication);

    @Operation(summary = "Deleta um usuário existente do banco de dados através do ID fornecido",
    description = "É necessário estar autenticado e possuir permissão ADMIN para acessar este método")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário deletado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado, exclusão não realizada"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    void deletarUsuario(Integer id);

    @Operation(summary = "Lista os usuários por permissão",
    description = "É necessário estar autenticado e possuir permissão suporte para acessar este método")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários filtrada pela permissão"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    List<ListarDadosUsuarioDTO> listarPermissao(Permissao permissao);
}
