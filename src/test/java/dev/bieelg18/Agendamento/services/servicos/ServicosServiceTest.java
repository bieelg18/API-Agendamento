package dev.bieelg18.Agendamento.services.servicos;

import dev.bieelg18.Agendamento.dtos.servicos.CriarServicoDTO;
import dev.bieelg18.Agendamento.dtos.servicos.EditarServicoDTO;
import dev.bieelg18.Agendamento.dtos.servicos.ListarServicoDTO;
import dev.bieelg18.Agendamento.dtos.servicos.ServicoAgendamentoDTO;
import dev.bieelg18.Agendamento.entities.Servicos;
import dev.bieelg18.Agendamento.enums.DuracaoServico;
import dev.bieelg18.Agendamento.enums.TipoServico;
import dev.bieelg18.Agendamento.exception.RecursoNaoEncontradoException;
import dev.bieelg18.Agendamento.mappers.servicos.CriarServicoMapper;
import dev.bieelg18.Agendamento.mappers.servicos.ListarServicoMapper;
import dev.bieelg18.Agendamento.repositories.ServicosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServicosServiceTest {

    @Mock
    private ServicosRepository servicosRepository;

    @Mock
    private CriarServicoMapper criarServicoMapper;

    @Mock
    private ListarServicoMapper listarServicoMapper;

    @InjectMocks
    private ServicosService servicosService;

    private Servicos servico;

    @BeforeEach
    void setUp(){

        servico = new Servicos();
        servico.setId(1);
        servico.setServico(TipoServico.CORTE);
        servico.setPreco(new BigDecimal("40.00"));
        servico.setDuracao(DuracaoServico.TRINTA_MINUTOS);

    }


    //Testes para o método de criar um novo serviço
    @Test
    void deveCriarUmNovoServico(){

        CriarServicoDTO criarDTO = new CriarServicoDTO(
                "CORTE",
                servico.getPreco(),
                "TRINTA_MINUTOS"
        );

        when(criarServicoMapper.toEntity(criarDTO))
                .thenReturn(servico);

        when(servicosRepository.save(servico))
                .thenReturn(servico);

        ListarServicoDTO dtoEsperado = new ListarServicoDTO(
                servico.getId(),
                servico.getServico(),
                servico.getPreco(),
                servico.getDuracao()
        );

        when(listarServicoMapper.toDTO(servico))
                .thenReturn(dtoEsperado);

        ListarServicoDTO resultado =
                servicosService.criarServico(criarDTO);

        assertNotNull(resultado);
        assertEquals(dtoEsperado, resultado);

        verify(servicosRepository).save(servico);
        verify(listarServicoMapper).toDTO(servico);

    }

    //Testes para listar todos os serviços
    @Test
    void deveListarTodosOsServicos(){

        when(servicosRepository.findAll())
                .thenReturn(List.of(servico));

        ListarServicoDTO dtoEsperado = new ListarServicoDTO(
                servico.getId(),
                servico.getServico(),
                servico.getPreco(),
                servico.getDuracao()
        );

        when(listarServicoMapper.toDTO(servico))
                .thenReturn(dtoEsperado);

        List<ListarServicoDTO> resultado =
                servicosService.listarServicos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(dtoEsperado, resultado.get(0));

        verify(servicosRepository).findAll();
        verify(listarServicoMapper).toDTO(servico);

    }

    @Test
    void deveRetornarUmaListaDeServicosVazia(){

        when(servicosRepository.findAll())
                .thenReturn(List.of());

        List<ListarServicoDTO> resultado =
                servicosService.listarServicos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(servicosRepository).findAll();
        verify(listarServicoMapper, never()).toDTO(any());

    }

    //Testes para editar um serviço
    @Test
    void deveEditarUmServico(){

        when(servicosRepository.findById(1))
                .thenReturn(Optional.of(servico));

        EditarServicoDTO editarDTO = new EditarServicoDTO(
                TipoServico.CORTE,
                new BigDecimal("50.00"),
                DuracaoServico.TRINTA_MINUTOS
        );

        when(servicosRepository.save(servico))
                .thenReturn(servico);

        ListarServicoDTO dtoEsperado = new ListarServicoDTO(
                1,
                TipoServico.CORTE,
                new BigDecimal("50.00"),
                DuracaoServico.TRINTA_MINUTOS
        );

        when(listarServicoMapper.toDTO(servico))
                .thenReturn(dtoEsperado);

        ListarServicoDTO resultado =
                servicosService.editarServico(1, editarDTO);

        assertNotNull(resultado);
        assertEquals(dtoEsperado, resultado);
        assertEquals(dtoEsperado.preco(), servico.getPreco());

        verify(servicosRepository).findById(1);
        verify(servicosRepository).save(servico);
        verify(listarServicoMapper).toDTO(servico);

    }

    @Test
    void naoDeveEditarServicoQuandoNaoAcharOId(){

        when(servicosRepository.findById(1))
                .thenReturn(Optional.empty());

        EditarServicoDTO editarDTO = new EditarServicoDTO(
                TipoServico.CORTE,
                new BigDecimal("50.00"),
                DuracaoServico.TRINTA_MINUTOS
        );

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> servicosService.editarServico(1, editarDTO)
        );

        assertEquals(
                "Serviço com o ID 1 não encontrado",
                exception.getMessage()
        );

        verify(servicosRepository).findById(1);
        verify(servicosRepository, never()).save(any());
        verify(listarServicoMapper, never()).toDTO(any());
    }

    //Testes para deletar um serviço
    @Test
    void deveDeletarServico(){

        when(servicosRepository.findById(1))
                .thenReturn(Optional.of(servico));

        servicosService.deletarServico(1);

        verify(servicosRepository).findById(1);
        verify(servicosRepository).delete(servico);
    }

    @Test
    void naoDeveDeletarServicoQuandoNaoAcharID(){

        when(servicosRepository.findById(1))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> servicosService.deletarServico(1)
        );

        assertEquals(
                "Serviço com o ID 1 não encontrado",
                exception.getMessage()
        );

        verify(servicosRepository).findById(1);
        verify(servicosRepository, never()).delete(any());



    }


}
