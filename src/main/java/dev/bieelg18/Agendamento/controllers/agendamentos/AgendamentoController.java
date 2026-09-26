package dev.bieelg18.Agendamento.controllers.agendamentos;

import dev.bieelg18.Agendamento.docs.agendamentos.AgendamentoControllerDocs;
import dev.bieelg18.Agendamento.dtos.agendamento.CriarAgendamentoDTO;
import dev.bieelg18.Agendamento.dtos.agendamento.ListarAgendamentoAdminDTO;
import dev.bieelg18.Agendamento.dtos.agendamento.ListarAgendamentoUsuarioDTO;
import dev.bieelg18.Agendamento.enums.StatusAgendamento;
import dev.bieelg18.Agendamento.services.agendamentos.AgendamentosService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/agendamentos")
public class AgendamentoController implements AgendamentoControllerDocs {

    private final AgendamentosService agendamentosService;


    //Rota para criar um novo agendamento
    @Override
    @PostMapping
    public ListarAgendamentoUsuarioDTO criarAgendamento(@RequestBody CriarAgendamentoDTO criarDTO, Authentication authentication){
        return agendamentosService.criarAgendamento(criarDTO, authentication);
    }

    //Rota para alterar o status de um agendamento
    @Override
    @PatchMapping("/status/{id}")
    public ListarAgendamentoUsuarioDTO statusAgendamento(@PathVariable Integer id, Authentication authentication){
        return agendamentosService.alterarStatus(id, authentication);
    }

    //Rota para listar todos os agendamentos
    @Override
    @GetMapping
    public List<ListarAgendamentoAdminDTO> agendamentosAdmin(){
        return agendamentosService.todosOsAgendamentos();
    }

    //Rota para listar todos os agendamentos do usuário que fizer a requisição
    @Override
    @GetMapping("/me")
    public List<ListarAgendamentoUsuarioDTO> agendamentos(Authentication authentication){
        return agendamentosService.listarAgendamentos(authentication);
    }

    //Rota para deletar um agendamento
    @Override
    @DeleteMapping("/{id}")
    public void deletarAgendamento(@PathVariable Integer id){
        agendamentosService.deletarAgendamento(id);
    }

    //Rota para listar os agendamentos pelo status
    @Override
    @GetMapping("/status")
    public List<ListarAgendamentoAdminDTO> listarStatus(@RequestParam StatusAgendamento status){
    return agendamentosService.agendamentoStatus(status);
    }

}
