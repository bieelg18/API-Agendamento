package dev.bieelg18.Agendamento.docs.agendamentos;

import dev.bieelg18.Agendamento.dtos.agendamento.CriarAgendamentoDTO;
import dev.bieelg18.Agendamento.dtos.agendamento.ListarAgendamentoAdminDTO;
import dev.bieelg18.Agendamento.dtos.agendamento.ListarAgendamentoUsuarioDTO;
import dev.bieelg18.Agendamento.enums.StatusAgendamento;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AgendamentoControllerDocs {

    @Operation(summary = "Cria um novo agendamento no banco de dados atrelado ao usuário que chamou a requisição",
    description = "É necessário estar autenticado para acessar este método")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Agendamento criado"),
            @ApiResponse(responseCode = "400", description = "Agendamento não criado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    ListarAgendamentoUsuarioDTO criarAgendamento(CriarAgendamentoDTO criarDTO, Authentication authentication);

    @Operation(summary = "Altera o status de um agendamento existente",
    description = "Somente usuários autenticados e com permissão PROFISSIONAL podem acessar este método, somente os profissionais atrelados ao agendamento conseguem alterar o status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status alterado"),
            @ApiResponse(responseCode = "409", description = "Você não pode alterar este agendamento pois ele não esta atrelado a você"),
            @ApiResponse(responseCode = "409", description = "O agendamento já esta com o status CONCLUIDO"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    ListarAgendamentoUsuarioDTO statusAgendamento(Integer id, Authentication authentication);

    @Operation(summary = "Lista todos os agendamentos existentes no banco de dados",
    description = "É necessário estar autenticado e possuir permissão ADMIN para acessar este recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista todos os agendamentos existentes"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    List<ListarAgendamentoAdminDTO> agendamentosAdmin();

    @Operation(summary = "Lista os agendamentos do usuário que chamou a requisição",
    description = "É necessário estar autenticado para acessar este método")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista com os agendamentos do usuário"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    List<ListarAgendamentoUsuarioDTO> agendamentos(Authentication authentication);

    @Operation(summary = "Deleta um agendamento existente através do ID fornecido",
    description = "É necessário estar autenticado e possuir permissão ADMIN para acessar este recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento deletado"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado, exclusão não realizada"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    void deletarAgendamento(Integer id);

    @Operation(summary = "Lista todos os agendamentos existentes no banco de dados filtrado pelo status do agendamento",
    description = "É necessário estar autenticado e possuir permissão ADMIN para acessar este recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista com todos os agendamentos existentes filtrada por status"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    List<ListarAgendamentoAdminDTO> listarStatus(StatusAgendamento status);

}
