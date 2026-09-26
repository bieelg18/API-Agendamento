package dev.bieelg18.Agendamento.docs.servicos;


import dev.bieelg18.Agendamento.dtos.servicos.CriarServicoDTO;
import dev.bieelg18.Agendamento.dtos.servicos.EditarServicoDTO;
import dev.bieelg18.Agendamento.dtos.servicos.ListarServicoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

public interface ServicosControllerDocs {

    @Operation(summary = "Cria um novo serviço no banco de dados",
    description = "É necessário estar autenticado e possuir permissão ADMIN para acessar este recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Serviço criado"),
            @ApiResponse(responseCode = "400", description = "Serviço não criado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    ListarServicoDTO criarServico(CriarServicoDTO criarDTO);

    @Operation(summary = "Lista todos os serviços presentes no banco de dados",
    description = "É necessário estar autenticado e possuir permissão ADMIN para acessar este recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de todos os serviços presentes no banco de dados"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
    })
    List<ListarServicoDTO> listarServicos();

   @Operation(summary = "Edita um serviço existente no banco de dados",
   description = "É necessário estar autenticado e possuir permissão ADMIN para acessar este recurso")
   @ApiResponses(value = {
           @ApiResponse(responseCode = "200", description = "Informações do serviço atualizadas"),
           @ApiResponse(responseCode = "404", description = "Serviço não encontrado, alteração não realizada"),
           @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
           @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
   })
    ListarServicoDTO editarServico(Integer id, EditarServicoDTO editarServicoDTO);

   @Operation(summary = "Deleta um serviço existente no banco de dados através do ID fornecido",
   description = "É necessário estar autenticado e possuir permissão ADMIN para acessar este recurso")
   @ApiResponses(value = {
           @ApiResponse(responseCode = "200", description = "Serviço deletado"),
           @ApiResponse(responseCode = "404", description = "Serviço não encontrado, exclusão não realizada"),
           @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
           @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este recurso")
   })
   void deletarServico(Integer id);

}
