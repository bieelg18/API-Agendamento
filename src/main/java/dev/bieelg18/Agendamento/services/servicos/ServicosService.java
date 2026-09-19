package dev.bieelg18.Agendamento.services.servicos;

import dev.bieelg18.Agendamento.dtos.servicos.CriarServicoDTO;
import dev.bieelg18.Agendamento.dtos.servicos.EditarServicoDTO;
import dev.bieelg18.Agendamento.dtos.servicos.ListarServicoDTO;
import dev.bieelg18.Agendamento.entities.Servicos;
import dev.bieelg18.Agendamento.exception.RecursoNaoEncontradoException;
import dev.bieelg18.Agendamento.mappers.servicos.CriarServicoMapper;
import dev.bieelg18.Agendamento.mappers.servicos.ListarServicoMapper;
import dev.bieelg18.Agendamento.repositories.ServicosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicosService {

    private final ServicosRepository servicosRepository;
    private final CriarServicoMapper criarServicoMapper;
    private final ListarServicoMapper listarServicoMapper;


    //Método para criar um novo serviço
    public ListarServicoDTO criarServico(CriarServicoDTO criarDTO){
        Servicos servico = criarServicoMapper.toEntity(criarDTO);
        Servicos servicoSalvo = servicosRepository.save(servico);
        return listarServicoMapper.toDTO(servicoSalvo);
    }

    //Método para listar todos os serviços
    public List<ListarServicoDTO> listarServicos(){
        List<Servicos> servicos = servicosRepository.findAll();
        return servicos.stream()
                .map(listarServicoMapper::toDTO)
                .toList();
    }

    //Método para editar um serviço
    public ListarServicoDTO editarServico(Integer id, EditarServicoDTO editarDTO){
        Servicos servico = servicosRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Serviço com o ID " + id + " não encontrado"
                ));
        if (editarDTO.servico() != null){
            servico.setServico(editarDTO.servico());
        }
        if (editarDTO.preco() != null){
            servico.setPreco(editarDTO.preco());
        }
        if (editarDTO.duracao() != null){
            servico.setDuracao(editarDTO.duracao());
        }
        Servicos servicoSalvo = servicosRepository.save(servico);
        return listarServicoMapper.toDTO(servicoSalvo);
    }

    //Método para deletar um serviço
    public void deletarServico(Integer id){
        Servicos servico = servicosRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Serviço com o ID " + id + " não encontrado"
                ));
        servicosRepository.delete(servico);
    }

}
