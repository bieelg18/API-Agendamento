package dev.bieelg18.Agendamento.services.agendamentos;

import dev.bieelg18.Agendamento.dtos.agendamento.CriarAgendamentoDTO;
import dev.bieelg18.Agendamento.dtos.agendamento.ListarAgendamentoAdminDTO;
import dev.bieelg18.Agendamento.dtos.agendamento.ListarAgendamentoUsuarioDTO;
import dev.bieelg18.Agendamento.entities.Agendamento;
import dev.bieelg18.Agendamento.entities.Servicos;
import dev.bieelg18.Agendamento.entities.Usuario;
import dev.bieelg18.Agendamento.enums.Permissao;
import dev.bieelg18.Agendamento.enums.StatusAgendamento;
import dev.bieelg18.Agendamento.exception.DataInvalidaException;
import dev.bieelg18.Agendamento.exception.PermissaoInvalidaException;
import dev.bieelg18.Agendamento.exception.RecursoNaoEncontradoException;
import dev.bieelg18.Agendamento.exception.StatusIncorretoException;
import dev.bieelg18.Agendamento.mappers.agendamento.CriarAgendamentoMapper;
import dev.bieelg18.Agendamento.mappers.agendamento.ListarAgendamentoAdminMapper;
import dev.bieelg18.Agendamento.mappers.agendamento.ListarAgendamentoUsuarioMapper;
import dev.bieelg18.Agendamento.repositories.AgendamentoRepository;
import dev.bieelg18.Agendamento.repositories.ServicosRepository;
import dev.bieelg18.Agendamento.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentosService {

    private final AgendamentoRepository agendamentoRepository;
    private final CriarAgendamentoMapper criarAgendamentoMapper;
    private final ListarAgendamentoAdminMapper listarAgendamentoAdminMapper;
    private final ListarAgendamentoUsuarioMapper listarAgendamentoUsuarioMapper;
    private final UsuarioRepository usuarioRepository;
    private final ServicosRepository servicosRepository;


    //Método para criar um novo agendamento
    public ListarAgendamentoUsuarioDTO criarAgendamento(CriarAgendamentoDTO criarDTO, Authentication authentication) {
        String email = authentication.getName();
        Usuario cliente = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário autenticado não encontrado"
                ));
        Usuario profissional = usuarioRepository.findById(criarDTO.idProfissional())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Profissional não encontrado"
                ));
        Servicos servico = servicosRepository.findById(criarDTO.idServico())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Serviço não encontrado"
                ));

        if (criarDTO.data().isBefore(LocalDateTime.now())) {
            throw new DataInvalidaException(
                    "A data de agendamento não pode ser anterior a data atual"
            );
        }

        LocalDateTime inicioNovo = criarDTO.data();

        LocalDateTime fimNovo = inicioNovo.plusMinutes(
                servico.getDuracao().getMinutos()
        );

        List<Agendamento> agendamentosProfissional = agendamentoRepository.findByProfissional(profissional);

        for (Agendamento existente : agendamentosProfissional) {

            LocalDateTime inicioExistente = existente.getData();

            LocalDateTime fimExistente = inicioExistente.plusMinutes(
                    existente.getServico()
                            .getDuracao()
                            .getMinutos()
            );

            if (inicioNovo.isBefore(fimExistente) && fimNovo.isAfter(inicioExistente)) {
                throw new DataInvalidaException(
                        "O profissional escolhido já possui um agendamento nesse período"
                );
            }

        }

        Agendamento agendamento = criarAgendamentoMapper.toEntity(criarDTO);
        agendamento.setCliente(cliente);
        agendamento.setProfissional(profissional);
        agendamento.setServico(servico);
        agendamento.setStatus(StatusAgendamento.AGENDADO);
        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);
        return listarAgendamentoUsuarioMapper.toDTO(agendamentoSalvo);

    }

    //Método para alterar o status de um agendamento
    public ListarAgendamentoUsuarioDTO alterarStatus(Integer id, Authentication authentication){

        String email = authentication.getName();
        Usuario profissional = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Profissional não encontrado"
                ));

        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Agendamento com o ID " + id + " não encontrado"
                ));

        if (!agendamento.getProfissional().getId().equals(profissional.getId())){
            throw new PermissaoInvalidaException(
                    "Você não possui permissão para alterar este agendamento"
            );
        }

        if (agendamento.getStatus() != StatusAgendamento.AGENDADO){
            throw new StatusIncorretoException(
                    "O agendamento já está concluído"
            );
        }
        agendamento.setStatus(StatusAgendamento.CONCLUIDO);
        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);
        return listarAgendamentoUsuarioMapper.toDTO(agendamentoSalvo);
    }

    //Método para listar todos os agendamentos
    public List<ListarAgendamentoAdminDTO> todosOsAgendamentos(){
        List<Agendamento> agendamentos = agendamentoRepository.findAll();
        return agendamentos.stream()
                .map(listarAgendamentoAdminMapper::toDTO)
                .toList();
    }

    //Método que vai listar todos os agendamentos do usuário que fizer a requisição
    public List<ListarAgendamentoUsuarioDTO> listarAgendamentos(Authentication authentication){
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário autenticado não encontrado"
                ));

        List<Agendamento> agendamentos;

        if (usuario.getPermissao() == Permissao.CLIENTE){
            agendamentos = agendamentoRepository.findByCliente(usuario);
        }else if (usuario.getPermissao() == Permissao.PROFISSIONAL){
            agendamentos = agendamentoRepository.findByProfissional(usuario);
        }else {
            throw new PermissaoInvalidaException(
                    "Permissão inválida para consultar agendamentos"
            );
        }
        return agendamentos.stream()
                .map(listarAgendamentoUsuarioMapper::toDTO)
                .toList();

    }

    //Método para deletar um agendamento
    public void deletarAgendamento(Integer id){
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Agendamento com o ID " + id + " não encontrado"
                ));
        agendamentoRepository.delete(agendamento);
    }

    //Método que vai listar os agendamentos pelo status
    public List<ListarAgendamentoAdminDTO> agendamentoStatus(StatusAgendamento statusAgendamento){
        List<Agendamento> agendamentos = agendamentoRepository.findByStatus(statusAgendamento);
        return agendamentos.stream()
                .map(listarAgendamentoAdminMapper::toDTO)
                .toList();
    }

}

