package dev.bieelg18.Agendamento.services.agendamentos;

import dev.bieelg18.Agendamento.dtos.agendamento.CriarAgendamentoDTO;
import dev.bieelg18.Agendamento.dtos.agendamento.ListarAgendamentoAdminDTO;
import dev.bieelg18.Agendamento.dtos.agendamento.ListarAgendamentoUsuarioDTO;
import dev.bieelg18.Agendamento.dtos.servicos.ServicoAgendamentoDTO;
import dev.bieelg18.Agendamento.dtos.usuarios.ProfissionalAgendamentoDTO;
import dev.bieelg18.Agendamento.entities.Agendamento;
import dev.bieelg18.Agendamento.entities.Servicos;
import dev.bieelg18.Agendamento.entities.Usuario;
import dev.bieelg18.Agendamento.enums.DuracaoServico;
import dev.bieelg18.Agendamento.enums.Permissao;
import dev.bieelg18.Agendamento.enums.StatusAgendamento;
import dev.bieelg18.Agendamento.enums.TipoServico;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AgendamentoServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CriarAgendamentoMapper criarAgendamentoMapper;

    @Mock
    private ListarAgendamentoAdminMapper listarAgendamentoAdminMapper;

    @Mock
    private ListarAgendamentoUsuarioMapper listarAgendamentoUsuarioMapper;

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private ServicosRepository servicosRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AgendamentosService agendamentosService;

    private Usuario cliente;
    private Usuario profissional;
    private Servicos servico;
    private Agendamento agendamento;

    @BeforeEach
    void setUp(){
        cliente = new Usuario();
        cliente.setId(1);
        cliente.setNome("Cliente Teste");
        cliente.setEmail("cliente@email.com");
        cliente.setPermissao(Permissao.CLIENTE);

        profissional = new Usuario();
        profissional.setId(2);
        profissional.setNome("Profissional Teste");
        profissional.setEmail("profissional@email.com");
        profissional.setPermissao(Permissao.PROFISSIONAL);

        servico = new Servicos();
        servico.setId(1);
        servico.setServico(TipoServico.CORTE);
        servico.setPreco(new BigDecimal("40.00"));
        servico.setDuracao(DuracaoServico.TRINTA_MINUTOS);

        agendamento = new Agendamento();
        agendamento.setId(1);
        agendamento.setCliente(cliente);
        agendamento.setProfissional(profissional);
        agendamento.setServico(servico);
        agendamento.setData(LocalDateTime.now().plusDays(1));
        agendamento.setStatus(StatusAgendamento.AGENDADO);
    }

    //Testes para o método criarAgendamento
    @Test
    void deveCriarAgendamentoComSucesso(){

        when(authentication.getName())
                .thenReturn("cliente@email.com");

        when(usuarioRepository.findByEmail("cliente@email.com"))
                .thenReturn(Optional.of(cliente));

        when(usuarioRepository.findById(2))
                .thenReturn(Optional.of(profissional));

        when(servicosRepository.findById(1))
                .thenReturn(Optional.of(servico));


        LocalDateTime dataAgendamento = LocalDateTime.now().plusDays(1);

        CriarAgendamentoDTO criarDTO = new CriarAgendamentoDTO(
                2,
                1,
                dataAgendamento
        );


        when(agendamentoRepository.findByProfissional(profissional))
                .thenReturn(List.of());

        Agendamento agendamento = new Agendamento();
        when(criarAgendamentoMapper.toEntity(criarDTO))
                .thenReturn(agendamento);

        when(agendamentoRepository.save(agendamento))
                .thenReturn(agendamento);

        ServicoAgendamentoDTO servicoDTO = new ServicoAgendamentoDTO(
                1,
                TipoServico.CORTE,
                new BigDecimal("40.00"),
                DuracaoServico.TRINTA_MINUTOS
        );

        ProfissionalAgendamentoDTO profissionalDTO = new ProfissionalAgendamentoDTO(
                2,
                "Profissional teste"
        );

        ListarAgendamentoUsuarioDTO dtoEsperado = new ListarAgendamentoUsuarioDTO(
                1,
                profissionalDTO,
                servicoDTO,
                dataAgendamento,
                StatusAgendamento.AGENDADO
        );

        when(listarAgendamentoUsuarioMapper.toDTO(agendamento))
                .thenReturn(dtoEsperado);

        ListarAgendamentoUsuarioDTO resultado =
                agendamentosService.criarAgendamento(criarDTO, authentication);

        assertNotNull(resultado);
        assertEquals(dtoEsperado, resultado);

        assertEquals(cliente, agendamento.getCliente());
        assertEquals(profissional, agendamento.getProfissional());
        assertEquals(servico, agendamento.getServico());
        assertEquals(StatusAgendamento.AGENDADO, agendamento.getStatus());

        verify(agendamentoRepository).save(agendamento);
    }

    @Test
    void naoDeveCriarAgendamentoComDataPassada() {

        when(authentication.getName())
                .thenReturn("cliente@email.com");

        when(usuarioRepository.findByEmail("cliente@email.com"))
                .thenReturn(Optional.of(cliente));


        when(usuarioRepository.findById(2))
                .thenReturn(Optional.of(profissional));

        when(servicosRepository.findById(1))
                .thenReturn(Optional.of(servico));

        LocalDateTime dataAgendamento = LocalDateTime.now().minusDays(1);

        CriarAgendamentoDTO criarDTO = new CriarAgendamentoDTO(
                2,
                1,
                dataAgendamento
        );

        assertThrows(
                DataInvalidaException.class,
                () -> agendamentosService.criarAgendamento(criarDTO, authentication)
        );

        verify(agendamentoRepository, never()).save(any());


    }

    @Test
    void naoDeveCriarAgendamentoQuandoProfissionalNaoExistir(){

        when(authentication.getName())
                .thenReturn("cliente@email.com");

        when(usuarioRepository.findByEmail("cliente@email.com"))
                .thenReturn(Optional.of(cliente));

        when(usuarioRepository.findById(3))
                .thenReturn(Optional.empty());

        LocalDateTime dataAgendamento = LocalDateTime.now().plusDays(1);

        CriarAgendamentoDTO criarDTO = new CriarAgendamentoDTO(
                3,
                1,
                dataAgendamento
        );


        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> agendamentosService.criarAgendamento(criarDTO, authentication)
        );

        assertEquals(
                "Profissional não encontrado",
                exception.getMessage()
        );

        verify(agendamentoRepository, never()).save(any());



    }

    @Test
    void naoDeveCriarAgendamentoQuandoUsuarioNaoExistir(){

        when(authentication.getName())
                .thenReturn("cliente@email.com");

        when(usuarioRepository.findByEmail("cliente@email.com"))
                .thenReturn(Optional.empty());

        LocalDateTime data = LocalDateTime.now().plusDays(1);

        CriarAgendamentoDTO criarDTO = new CriarAgendamentoDTO(
                2,
                1,
                data
        );

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> agendamentosService.criarAgendamento(criarDTO, authentication)
        );

        assertEquals(
                "Usuário autenticado não encontrado",
                exception.getMessage()
        );

        verify(agendamentoRepository, never()).save(any());

    }

    @Test
    void naoDeveCriarAgendamentoQuandoServicoNaoExistir(){

        when(authentication.getName())
                .thenReturn("cliente@email.com");

        when(usuarioRepository.findByEmail("cliente@email.com"))
                .thenReturn(Optional.of(cliente));

        when(usuarioRepository.findById(2))
                .thenReturn(Optional.of(profissional));

        when(servicosRepository.findById(1))
                .thenReturn(Optional.empty());

        CriarAgendamentoDTO criarDTO = new CriarAgendamentoDTO(
                2,
                1,
                LocalDateTime.now().plusDays(1)
        );


        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> agendamentosService.criarAgendamento(criarDTO, authentication)
        );

        assertEquals(
                "Serviço não encontrado",
                exception.getMessage()
        );

        verify(agendamentoRepository, never()).save(any());

    }


    @Test
    void naoDeveCriarAgendamentoQuandoProfissionalJaPossuirAgendamentoNoPeriodo(){

        when(authentication.getName())
                .thenReturn("cliente@email.com");

        when(usuarioRepository.findByEmail("cliente@email.com"))
                .thenReturn(Optional.of(cliente));

        when(usuarioRepository.findById(2))
                .thenReturn(Optional.of(profissional));

        when(servicosRepository.findById(1))
                .thenReturn(Optional.of(servico));


        LocalDateTime inicioExistente = LocalDateTime.now()
                .plusDays(2)
                .withHour(14)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        Agendamento agendamentoExistente = new Agendamento();
        agendamentoExistente.setId(1);
        agendamentoExistente.setProfissional(profissional);
        agendamentoExistente.setServico(servico);
        agendamentoExistente.setData(inicioExistente);
        agendamentoExistente.setStatus(StatusAgendamento.AGENDADO);

        when(agendamentoRepository.findByProfissional(profissional))
                .thenReturn(List.of(agendamentoExistente));

        LocalDateTime inicioNovo = inicioExistente.plusMinutes(15);

        CriarAgendamentoDTO criarDTO = new CriarAgendamentoDTO(
                2,
                1,
                inicioNovo
        );

        DataInvalidaException exception = assertThrows(
                DataInvalidaException.class,
                () -> agendamentosService.criarAgendamento(criarDTO, authentication)
        );

        assertEquals(
                "O profissional escolhido já possui um agendamento nesse período",
                exception.getMessage()
        );

        verify(agendamentoRepository, never()).save(any());

    }

    @Test
    void deveCriarAgendamentoQuandoIniciarNoHorarioDeTerminoDoAnterior(){

        when(authentication.getName())
                .thenReturn("cliente@email.com");

        when(usuarioRepository.findByEmail("cliente@email.com"))
                .thenReturn(Optional.of(cliente));

         when(usuarioRepository.findById(2))
                .thenReturn(Optional.of(profissional));

        when(servicosRepository.findById(1))
                .thenReturn(Optional.of(servico));


        LocalDateTime inicioExistente = LocalDateTime.now()
                .plusDays(2)
                .withHour(14)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        Agendamento agendamentoExistente = new Agendamento();
        agendamentoExistente.setId(1);
        agendamentoExistente.setProfissional(profissional);
        agendamentoExistente.setServico(servico);
        agendamentoExistente.setData(inicioExistente);
        agendamentoExistente.setStatus(StatusAgendamento.AGENDADO);

        when(agendamentoRepository.findByProfissional(profissional))
                .thenReturn(List.of(agendamentoExistente));

        LocalDateTime inicioNovo = inicioExistente.plusMinutes(30);

        CriarAgendamentoDTO criarDTO = new CriarAgendamentoDTO(
                2,
                1,
                inicioNovo
        );

        Agendamento agendamento = new Agendamento();
        when(criarAgendamentoMapper.toEntity(criarDTO))
                .thenReturn(agendamento);

        when(agendamentoRepository.save(agendamento))
                .thenReturn(agendamento);

        ServicoAgendamentoDTO servicoDTO = new ServicoAgendamentoDTO(
                1,
                TipoServico.CORTE,
                new BigDecimal("40.00"),
                DuracaoServico.TRINTA_MINUTOS
        );

        ProfissionalAgendamentoDTO profissionalDTO = new ProfissionalAgendamentoDTO(
                2,
                "Profissional Teste"
        );

        ListarAgendamentoUsuarioDTO dtoEsperado = new ListarAgendamentoUsuarioDTO(
                1,
                profissionalDTO,
                servicoDTO,
                inicioNovo,
                StatusAgendamento.AGENDADO
        );

        when(listarAgendamentoUsuarioMapper.toDTO(agendamento))
                .thenReturn(dtoEsperado);

        ListarAgendamentoUsuarioDTO resultado =
                agendamentosService.criarAgendamento(criarDTO, authentication);

        assertNotNull(resultado);
        assertEquals(dtoEsperado, resultado);

        assertEquals(cliente, agendamento.getCliente());
        assertEquals(profissional, agendamento.getProfissional());
        assertEquals(servico, agendamento.getServico());
        assertEquals(StatusAgendamento.AGENDADO, agendamento.getStatus());

        verify(agendamentoRepository).save(agendamento);

    }

    //Testes para o método alterarStatus
    @Test
    void deveAlterarStatusAgendamento(){

        when(authentication.getName())
                .thenReturn("profissional@email.com");

        profissional.setId(1);

        when(usuarioRepository.findByEmail("profissional@email.com"))
                .thenReturn(Optional.of(profissional));

        cliente.setId(2);


        when(agendamentoRepository.findById(1))
                .thenReturn(Optional.of(agendamento));

        when(agendamentoRepository.save(agendamento))
                .thenReturn(agendamento);

        ServicoAgendamentoDTO servicoDTO = new ServicoAgendamentoDTO(
                1,
                TipoServico.CORTE,
                new BigDecimal("40.00"),
                DuracaoServico.TRINTA_MINUTOS
        );

        ProfissionalAgendamentoDTO profissionalDTO = new ProfissionalAgendamentoDTO(
                1,
                "Profissional Teste"
        );

        ListarAgendamentoUsuarioDTO dtoEsperado = new ListarAgendamentoUsuarioDTO(
                1,
                profissionalDTO,
                servicoDTO,
                agendamento.getData(),
                StatusAgendamento.CONCLUIDO
        );

        when(listarAgendamentoUsuarioMapper.toDTO(agendamento))
                .thenReturn(dtoEsperado);

        ListarAgendamentoUsuarioDTO resultado =
                agendamentosService.alterarStatus(1, authentication);

        assertNotNull(resultado);
        assertEquals(dtoEsperado, resultado);

        assertEquals(StatusAgendamento.CONCLUIDO, agendamento.getStatus());

        verify(agendamentoRepository).save(agendamento);

    }

    @Test
    void naoDeveAlterarStatusSeProfissionalNaoForEncontrado(){

        when(authentication.getName())
                .thenReturn("profissional@email.com");

        when(usuarioRepository.findByEmail("profissional@email.com"))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> agendamentosService.alterarStatus(1, authentication)
        );

        assertEquals(
                "Profissional não encontrado",
                exception.getMessage()
        );

        verify(agendamentoRepository, never()).save(any());

    }

    @Test
    void naoDeveAlterarStatusQuandoAgendamentoNaoExistir(){

        when(authentication.getName())
                .thenReturn("profissional@email.com");

        profissional.setId(1);

        when(usuarioRepository.findByEmail("profissional@email.com"))
                .thenReturn(Optional.of(profissional));

        when(agendamentoRepository.findById(1))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> agendamentosService.alterarStatus(1, authentication)
        );

        assertEquals(
                "Agendamento com o ID " + 1 + " não encontrado",
                exception.getMessage()
        );

        verify(agendamentoRepository, never()).save(any());

    }

    @Test
    void naoDeveAlterarStatusDeAgendamentoQueNaoPertenceAoProfissional(){

        when(authentication.getName())
                .thenReturn("profissional2@email.com");


        Usuario autenticado = new Usuario();
        autenticado.setId(1);
        autenticado.setNome("Profissional Autenticado");
        autenticado.setEmail("profissional2@email.com");
        autenticado.setPermissao(Permissao.PROFISSIONAL);

        when(usuarioRepository.findByEmail("profissional2@email.com"))
                .thenReturn(Optional.of(autenticado));

        profissional.setId(2);

        cliente.setId(3);

        when(agendamentoRepository.findById(1))
                .thenReturn(Optional.of(agendamento));

        PermissaoInvalidaException exception = assertThrows(
                PermissaoInvalidaException.class,
                () -> agendamentosService.alterarStatus(1, authentication)
        );

        assertEquals(
                "Você não possui permissão para alterar este agendamento",
                exception.getMessage()
        );

        verify(agendamentoRepository, never()).save(any());
    }

    @Test
    void naoDeveAlterarStatusDeAgendamentoJaConcluido(){

        when(authentication.getName())
                .thenReturn("profissional@email.com");

        profissional.setId(1);

        when(usuarioRepository.findByEmail("profissional@email.com"))
                .thenReturn(Optional.of(profissional));


        cliente.setId(2);


        agendamento.setStatus(StatusAgendamento.CONCLUIDO);

        when(agendamentoRepository.findById(1))
                .thenReturn(Optional.of(agendamento));

        StatusIncorretoException exception = assertThrows(
                StatusIncorretoException.class,
                () -> agendamentosService.alterarStatus(1, authentication)
        );

        assertEquals(
                "O agendamento já está concluído",
                exception.getMessage()
        );

        verify(agendamentoRepository, never()).save(any());

    }

    //Testes para o método de listar todos os agendamentos
    @Test
    void deveListarTodosOsAgendamentos(){

        when(agendamentoRepository.findAll())
                .thenReturn(List.of(agendamento));

        ListarAgendamentoAdminDTO dto = new ListarAgendamentoAdminDTO(
                1,
                agendamento.getCliente().getId(),
                agendamento.getProfissional().getId(),
                agendamento.getServico().getId(),
                agendamento.getData(),
                agendamento.getStatus()

        );

        when(listarAgendamentoAdminMapper.toDTO(agendamento))
                .thenReturn(dto);


        List<ListarAgendamentoAdminDTO> resultado =
                agendamentosService.todosOsAgendamentos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(dto, resultado.get(0));

        verify(agendamentoRepository).findAll();
        verify(listarAgendamentoAdminMapper).toDTO(agendamento);

    }

    @Test
    void deveRetornarListaVaziaDeAgendamentos(){

        when(agendamentoRepository.findAll())
                .thenReturn(List.of());

        List<ListarAgendamentoAdminDTO> resultado =
                agendamentosService.todosOsAgendamentos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(agendamentoRepository).findAll();



    }

    //Testes para método de listar agendamentos do usuário que chamou a requisição
    @Test
    void deveListarAgendamentosDoClienteQueChamouARequisicao(){

        when(authentication.getName())
                .thenReturn("cliente@email.com");

        when(usuarioRepository.findByEmail("cliente@email.com"))
                .thenReturn(Optional.of(cliente));

        when(agendamentoRepository.findByCliente(cliente))
                .thenReturn(List.of(agendamento));

        ServicoAgendamentoDTO servicoDTO = new ServicoAgendamentoDTO(
                servico.getId(),
                servico.getServico(),
                servico.getPreco(),
                servico.getDuracao()
        );

        ProfissionalAgendamentoDTO profissionalDTO = new ProfissionalAgendamentoDTO(
                profissional.getId(),
                profissional.getNome()
        );

        ListarAgendamentoUsuarioDTO dto = new ListarAgendamentoUsuarioDTO(
                agendamento.getId(),
                profissionalDTO,
                servicoDTO,
                agendamento.getData(),
                agendamento.getStatus()
        );

        when(listarAgendamentoUsuarioMapper.toDTO(agendamento))
                .thenReturn(dto);

        List<ListarAgendamentoUsuarioDTO> resultado =
                agendamentosService.listarAgendamentos(authentication);


        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(dto, resultado.get(0));


        verify(usuarioRepository).findByEmail("cliente@email.com");
        verify(agendamentoRepository).findByCliente(cliente);
        verify(listarAgendamentoUsuarioMapper).toDTO(agendamento);

    }

    @Test
    void deveListarAgendamentosDoProfissionalQueChamouARequisicao(){

        when(authentication.getName())
                .thenReturn("profissional@email.com");

        when(usuarioRepository.findByEmail("profissional@email.com"))
                .thenReturn(Optional.of(profissional));

        when(agendamentoRepository.findByProfissional(profissional))
                .thenReturn(List.of(agendamento));

        ServicoAgendamentoDTO servicoDTO = new ServicoAgendamentoDTO(
                servico.getId(),
                servico.getServico(),
                servico.getPreco(),
                servico.getDuracao()
        );

        ProfissionalAgendamentoDTO profissionalDTO = new ProfissionalAgendamentoDTO(
                profissional.getId(),
                profissional.getNome()
        );

        ListarAgendamentoUsuarioDTO dto = new ListarAgendamentoUsuarioDTO(
                agendamento.getId(),
                profissionalDTO,
                servicoDTO,
                agendamento.getData(),
                agendamento.getStatus()
        );

        when(listarAgendamentoUsuarioMapper.toDTO(agendamento))
                .thenReturn(dto);

        List<ListarAgendamentoUsuarioDTO> resultado =
                agendamentosService.listarAgendamentos(authentication);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(dto, resultado.get(0));

        verify(usuarioRepository).findByEmail("profissional@email.com");
        verify(agendamentoRepository).findByProfissional(profissional);
        verify(listarAgendamentoUsuarioMapper).toDTO(agendamento);

    }

    @Test
    void naoDeveListarAgendamentoQuandoUsuarioNaoForEncontrado(){

        when(authentication.getName())
                .thenReturn("cliente@email.com");

        when(usuarioRepository.findByEmail("cliente@email.com"))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> agendamentosService.listarAgendamentos(authentication)
        );

        assertEquals(
                "Usuário autenticado não encontrado",
                exception.getMessage()
        );

        verify(agendamentoRepository, never()).findByCliente(any());
        verify(agendamentoRepository, never()).findByProfissional(any());

    }

    @Test
    void naoDeveListarAgendamentosSeAPermissaoNaoForClienteOuProfissional(){

        when(authentication.getName())
                .thenReturn("cliente@email.com");

        when(usuarioRepository.findByEmail("cliente@email.com"))
                .thenReturn(Optional.of(cliente));

        cliente.setPermissao(Permissao.ADMIN);

        PermissaoInvalidaException exception = assertThrows(
                PermissaoInvalidaException.class,
                () -> agendamentosService.listarAgendamentos(authentication)
        );

        assertEquals(
                "Permissão inválida para consultar agendamentos",
                exception.getMessage()
        );

        verify(agendamentoRepository, never()).findByCliente(any());
        verify(agendamentoRepository, never()).findByProfissional(any());

    }

    @Test
    void deveListarAgendamentosComoListaVazia(){

        when(authentication.getName())
                .thenReturn("cliente@email.com");

        when(usuarioRepository.findByEmail("cliente@email.com"))
                .thenReturn(Optional.of(cliente));

        when(agendamentoRepository.findByCliente(cliente))
                .thenReturn(List.of());

        List<ListarAgendamentoUsuarioDTO> resultado =
                agendamentosService.listarAgendamentos(authentication);

        assertNotNull(resultado);
        assertEquals(0, resultado.size());
        assertTrue(resultado.isEmpty());

        verify(agendamentoRepository).findByCliente(cliente);

    }

    //Teste para o método de deletar agendamento
    @Test
    void deveDeletarAgendamento(){

        when(agendamentoRepository.findById(1))
                .thenReturn(Optional.of(agendamento));

         agendamentosService.deletarAgendamento(1);

         verify(agendamentoRepository).findById(1);
         verify(agendamentoRepository).delete(agendamento);

    }

    @Test
    void naoDeveDeletarAgendamentoQuandoNaoExistirAgendamento(){

        when(agendamentoRepository.findById(1))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> agendamentosService.deletarAgendamento(1)
        );

        assertEquals(
                "Agendamento com o ID " + 1 + " não encontrado",
                exception.getMessage()
        );

        verify(agendamentoRepository).findById(1);
        verify(agendamentoRepository, never()).delete(any());

    }

    //Testes para listar agendamentos por status
    @Test
    void deveListarAgendamentosPorStatus(){

        when(agendamentoRepository.findByStatus(StatusAgendamento.AGENDADO))
                .thenReturn(List.of(agendamento));

        ListarAgendamentoAdminDTO dto = new ListarAgendamentoAdminDTO(
                agendamento.getId(),
                cliente.getId(),
                profissional.getId(),
                servico.getId(),
                agendamento.getData(),
                agendamento.getStatus()
        );

        when(listarAgendamentoAdminMapper.toDTO(agendamento))
                .thenReturn(dto);

        List<ListarAgendamentoAdminDTO> resultado =
                agendamentosService.agendamentoStatus(StatusAgendamento.AGENDADO);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(dto, resultado.get(0));

        verify(agendamentoRepository).findByStatus(StatusAgendamento.AGENDADO);
        verify(listarAgendamentoAdminMapper).toDTO(agendamento);

    }

    @Test
    void deveRetornarUmaListaVaziaDeAgendamentos(){

        when(agendamentoRepository.findByStatus(StatusAgendamento.AGENDADO))
                .thenReturn(List.of());

        List<ListarAgendamentoAdminDTO> resultado =
                agendamentosService.agendamentoStatus(StatusAgendamento.AGENDADO);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(agendamentoRepository).findByStatus(StatusAgendamento.AGENDADO);
        verify(listarAgendamentoAdminMapper, never()).toDTO(any());

    }

}
