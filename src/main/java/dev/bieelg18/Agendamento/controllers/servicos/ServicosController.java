package dev.bieelg18.Agendamento.controllers.servicos;

import dev.bieelg18.Agendamento.dtos.servicos.CriarServicoDTO;
import dev.bieelg18.Agendamento.dtos.servicos.EditarServicoDTO;
import dev.bieelg18.Agendamento.dtos.servicos.ListarServicoDTO;
import dev.bieelg18.Agendamento.services.servicos.ServicosService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/servicos")
public class ServicosController {

    private final ServicosService servicosService;


    //Rota para criar um novo serviço
    @PostMapping
    public ListarServicoDTO criarServico(@RequestBody CriarServicoDTO criarDTO){
        return servicosService.criarServico(criarDTO);
    }

    //Rota listar todos os serviços
    @GetMapping
    public List<ListarServicoDTO> listarServicos(){
        return servicosService.listarServicos();
    }

    //Rota para editar um serviço
    @PatchMapping("/{id}")
    public ListarServicoDTO editarServico(@PathVariable Integer id, @RequestBody EditarServicoDTO editarDTO){
        return servicosService.editarServico(id, editarDTO);
    }

    //Rota para deletar um serviço
    @DeleteMapping("/id")
    public void deletarServico(@PathVariable Integer id){
        servicosService.deletarServico(id);
    }

}
