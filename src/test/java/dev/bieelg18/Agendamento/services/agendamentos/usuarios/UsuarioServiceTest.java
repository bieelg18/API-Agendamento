package dev.bieelg18.Agendamento.services.agendamentos.usuarios;

import dev.bieelg18.Agendamento.dtos.usuarios.CriarUsuarioDTO;
import dev.bieelg18.Agendamento.dtos.usuarios.EditarDadosUsuarioDTO;
import dev.bieelg18.Agendamento.dtos.usuarios.EditarPermissaoDTO;
import dev.bieelg18.Agendamento.dtos.usuarios.ListarDadosUsuarioDTO;
import dev.bieelg18.Agendamento.entities.Usuario;
import dev.bieelg18.Agendamento.enums.Permissao;
import dev.bieelg18.Agendamento.exception.RecursoNaoEncontradoException;
import dev.bieelg18.Agendamento.mappers.usuario.CriarUsuarioMapper;
import dev.bieelg18.Agendamento.mappers.usuario.ListarDadosUsuarioMapper;
import dev.bieelg18.Agendamento.repositories.UsuarioRepository;
import dev.bieelg18.Agendamento.services.usuarios.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CriarUsuarioMapper criarUsuarioMapper;

    @Mock
    private ListarDadosUsuarioMapper listarDadosUsuarioMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp(){

        usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("Usuario Teste");
        usuario.setEmail("usuario@email.com");
        usuario.setPermissao(Permissao.CLIENTE);
    }


    //Testes do método de criar um novo usuário
    @Test
    void deveCriarUmNovoUsuario(){

        CriarUsuarioDTO criarDTO = new CriarUsuarioDTO(
                usuario.getNome(),
                usuario.getEmail(),
                "senha123",
                "CLIENTE"
        );

        when(criarUsuarioMapper.toEntity(criarDTO))
                .thenReturn(usuario);

        when(passwordEncoder.encode("senha123"))
                .thenReturn("senhaCriptografada");

        when(usuarioRepository.save(usuario))
                .thenReturn(usuario);

        ListarDadosUsuarioDTO dtoEsperado = new ListarDadosUsuarioDTO(
                1,
                criarDTO.nome(),
                criarDTO.email(),
                criarDTO.permissao()
        );

        when(listarDadosUsuarioMapper.toDTO(usuario))
                .thenReturn(dtoEsperado);

        ListarDadosUsuarioDTO resultado =
                usuarioService.criarUsuario(criarDTO);

        assertNotNull(resultado);
        assertEquals(dtoEsperado, resultado);

        assertEquals("senhaCriptografada", usuario.getSenha());

        verify(passwordEncoder).encode("senha123");
        verify(usuarioRepository).save(usuario);

    }


    //Testes para listar todos os usuários
    @Test
    void deveListarTodosOsUsuarios(){

        when(usuarioRepository.findAll())
                .thenReturn(List.of(usuario));

        ListarDadosUsuarioDTO dtoEsperado = new ListarDadosUsuarioDTO(
                1,
                usuario.getNome(),
                usuario.getEmail(),
                "CLIENTE"
        );

        when(listarDadosUsuarioMapper.toDTO(usuario))
                .thenReturn(dtoEsperado);

        List<ListarDadosUsuarioDTO> resultado =
                usuarioService.listarUsuarios();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(dtoEsperado, resultado.get(0));

        verify(usuarioRepository).findAll();
        verify(listarDadosUsuarioMapper).toDTO(usuario);

    }

    @Test
    void deveRetornarListaVaziaDeUsuarios(){

        when(usuarioRepository.findAll())
                .thenReturn(List.of());

        List<ListarDadosUsuarioDTO> resultado =
                usuarioService.listarUsuarios();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(usuarioRepository).findAll();
    }

    //Testes para buscar usuário por e-mail
    @Test
    void deveRetornarUsuarioPeloEmailFornecido(){

        when(usuarioRepository.findByEmail("usuario@email.com"))
                .thenReturn(Optional.of(usuario));

        ListarDadosUsuarioDTO dtoEsperado = new ListarDadosUsuarioDTO(
                1,
                usuario.getNome(),
                usuario.getEmail(),
                "CLIENTE"
        );

        when(listarDadosUsuarioMapper.toDTO(usuario))
                .thenReturn(dtoEsperado);

        ListarDadosUsuarioDTO resultado =
                usuarioService.buscarEmail("usuario@email.com");

        assertNotNull(resultado);
        assertEquals(dtoEsperado, resultado);

        verify(usuarioRepository).findByEmail("usuario@email.com");
        verify(listarDadosUsuarioMapper).toDTO(usuario);

    }

    @Test
    void naoDeveRetornarUsuarioQuandoEmailNaoForEncontrado(){

        when(usuarioRepository.findByEmail("usuario@email.com"))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> usuarioService.buscarEmail("usuario@email.com")
        );

        assertEquals(
                "Usuário com o e-mail usuario@email.com não encontrado",
                exception.getMessage()
        );

        verify(usuarioRepository).findByEmail("usuario@email.com");
        verify(listarDadosUsuarioMapper, never()).toDTO(any());

    }

    //Testes para alterar a permissão de um usuário
    @Test
    void deveAlterarAPermissaoDeUmUsuario(){

        when(usuarioRepository.findById(1))
                .thenReturn(Optional.of(usuario));

        EditarPermissaoDTO permissaoDTO = new EditarPermissaoDTO(
                Permissao.PROFISSIONAL
        );

        when(usuarioRepository.save(usuario))
                .thenReturn(usuario);

        ListarDadosUsuarioDTO dtoEsperado = new ListarDadosUsuarioDTO(
                1,
                usuario.getNome(),
                usuario.getEmail(),
                "PROFISSIONAL"
        );

        when(listarDadosUsuarioMapper.toDTO(usuario))
                .thenReturn(dtoEsperado);

        ListarDadosUsuarioDTO resultado =
                usuarioService.editarPermissao(1,permissaoDTO);

        assertNotNull(resultado);
        assertEquals(dtoEsperado, resultado);
        assertEquals(Permissao.PROFISSIONAL, usuario.getPermissao());

        verify(usuarioRepository).findById(1);
        verify(usuarioRepository).save(usuario);
        verify(listarDadosUsuarioMapper).toDTO(usuario);

    }

    @Test
    void naoDeveAlterarPermissaoSeUsuarioNaoExistir(){

        when(usuarioRepository.findById(1))
                .thenReturn(Optional.empty());

        EditarPermissaoDTO permissaoDTO = new EditarPermissaoDTO(
                Permissao.PROFISSIONAL
        );

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> usuarioService.editarPermissao(1, permissaoDTO)
        );

        assertEquals(
                "Usuário com o ID 1 não encontrado",
                exception.getMessage()
        );

        verify(usuarioRepository).findById(1);
        verify(usuarioRepository, never()).save(any());
        verify(listarDadosUsuarioMapper, never()).toDTO(any());

    }

    //Testes para editar dados do usuário que chamar a requisição
    @Test
    void deveEditarDadosDoUsuario(){

        when(authentication.getName())
                .thenReturn("usuario@email.com");

        when(usuarioRepository.findByEmail("usuario@email.com"))
                .thenReturn(Optional.of(usuario));

        EditarDadosUsuarioDTO editarDTO = new EditarDadosUsuarioDTO(
                "Usuario Editado",
                "editado@email.com",
                "senha456"
        );

        when(passwordEncoder.encode("senha456"))
                .thenReturn("senhaCriptografada");

        when(usuarioRepository.save(usuario))
                .thenReturn(usuario);

        ListarDadosUsuarioDTO dtoEsperado = new ListarDadosUsuarioDTO(
                1,
                editarDTO.nome(),
                editarDTO.email(),
                "CLIENTE"
        );

        when(listarDadosUsuarioMapper.toDTO(usuario))
                .thenReturn(dtoEsperado);

        ListarDadosUsuarioDTO resultado =
                usuarioService.editarDadosCadastro(editarDTO, authentication);

        assertNotNull(resultado);
        assertEquals(dtoEsperado, resultado);

        assertEquals(dtoEsperado.nome(), usuario.getNome());
        assertEquals(dtoEsperado.email(), usuario.getEmail());
        assertEquals("senhaCriptografada", usuario.getSenha());

        verify(usuarioRepository).findByEmail("usuario@email.com");
        verify(passwordEncoder).encode("senha456");
        verify(usuarioRepository).save(usuario);
        verify(listarDadosUsuarioMapper).toDTO(usuario);

    }

    @Test
    void naoDeveAlterarDadosDoUsuarioQueNaoExistir(){

        when(authentication.getName())
                .thenReturn("usuario@email.com");

        when(usuarioRepository.findByEmail("usuario@email.com"))
                .thenReturn(Optional.empty());

        EditarDadosUsuarioDTO editarDTO = new EditarDadosUsuarioDTO(
                "Usuario Editado",
                "editado@email.com",
                "senha456"
        );

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> usuarioService.editarDadosCadastro(editarDTO, authentication)
        );

        assertEquals(
                "Usuário com e-mail usuario@email.com não encontrado",
                exception.getMessage()
        );

        verify(usuarioRepository).findByEmail("usuario@email.com");
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).save(any());
        verify(listarDadosUsuarioMapper, never()).toDTO(any());

    }

    //Testes para deletar usuário
    @Test
    void deveDeletarUsuario(){

        when(usuarioRepository.findById(1))
                .thenReturn(Optional.of(usuario));

        usuarioService.deletarUsuario(1);

        verify(usuarioRepository).findById(1);
        verify(usuarioRepository).delete(usuario);

    }

    @Test
    void naoDeveDeletarUsuarioQuandoIdNaoForEncontrado(){

        when(usuarioRepository.findById(1))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> usuarioService.deletarUsuario(1)
        );

        assertEquals(
                "Usuário com o ID 1 não encontrado",
                exception.getMessage()
        );

        verify(usuarioRepository).findById(1);
        verify(usuarioRepository, never()).delete(any());

    }

    //Testes para listar usuários pela permissão
    @Test
    void deveListarUsuariosPelaPermissao(){

        when(usuarioRepository.findByPermissao(Permissao.CLIENTE))
                .thenReturn(List.of(usuario));

        ListarDadosUsuarioDTO dtoEsperado = new ListarDadosUsuarioDTO(
                1,
                usuario.getNome(),
                usuario.getEmail(),
                "CLIENTE"
        );

        when(listarDadosUsuarioMapper.toDTO(usuario))
                .thenReturn(dtoEsperado);

        List<ListarDadosUsuarioDTO> resultado =
                usuarioService.listarPermissao(Permissao.CLIENTE);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(dtoEsperado, resultado.get(0));

        verify(usuarioRepository).findByPermissao(Permissao.CLIENTE);
        verify(listarDadosUsuarioMapper).toDTO(usuario);

    }

    @Test
    void deveRetornarUmaListaVaziaDeUsuariosPelaPermissao(){

        when(usuarioRepository.findByPermissao(Permissao.CLIENTE))
                .thenReturn(List.of());

        List<ListarDadosUsuarioDTO> resultado =
                usuarioService.listarPermissao(Permissao.CLIENTE);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(usuarioRepository).findByPermissao(Permissao.CLIENTE);
        verify(listarDadosUsuarioMapper, never()).toDTO(any());

    }

}
