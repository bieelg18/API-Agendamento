package dev.bieelg18.Agendamento.services.usuarios;

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
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CriarUsuarioMapper criarUsuarioMapper;
    private final ListarDadosUsuarioMapper listarDadosUsuarioMapper;
    private final PasswordEncoder passwordEncoder;


    //Método para criar um novo usuário
    public ListarDadosUsuarioDTO criarUsuario(CriarUsuarioDTO criarDTO){
        Usuario usuario = criarUsuarioMapper.toEntity(criarDTO);
        usuario.setSenha(passwordEncoder.encode(criarDTO.senha()));
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return listarDadosUsuarioMapper.toDTO(usuarioSalvo);
    }

    //Método para listar todos os usuários
    public List<ListarDadosUsuarioDTO> listarUsuarios(){
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(listarDadosUsuarioMapper::toDTO)
                .toList();
    }

    //Método para buscar usuário pelo e-mail
    public ListarDadosUsuarioDTO buscarEmail(String email){
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário com o e-mail " + email + " não encontrado"
                ));
        return listarDadosUsuarioMapper.toDTO(usuario);
    }

    //Método para alterar a permissão de um usuário
    public ListarDadosUsuarioDTO editarPermissao(Integer id, EditarPermissaoDTO permissaoDTO){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário com o ID " + id + " não encontrado"
                ));
        usuario.setPermissao(permissaoDTO.permissao());
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return listarDadosUsuarioMapper.toDTO(usuarioSalvo);
    }

    //Método para editar dados do usuário que chamar a requisição
    public ListarDadosUsuarioDTO editarDadosCadastro(EditarDadosUsuarioDTO editarDTO, Authentication authentication){
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário com e-mail " + email + " não encontrado"
                ));
        if (editarDTO.nome() != null){
            usuario.setNome(editarDTO.nome());
        }
        if (editarDTO.email() != null){
            usuario.setEmail(editarDTO.email());
        }
        if (editarDTO.senha() != null){
            usuario.setSenha(passwordEncoder.encode(editarDTO.senha()));
        }
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return listarDadosUsuarioMapper.toDTO(usuarioSalvo);
    }

    //Método para deletar um usuário pelo id
    public void deletarUsuario(Integer id){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário com o ID " + id + " não encontrado"
                ));
        usuarioRepository.delete(usuario);
    }

    //Método para listar usuários pela permissão
    public List<ListarDadosUsuarioDTO> listarPermissao(Permissao permissao){
        List<Usuario> usuarios = usuarioRepository.findByPermissao(permissao);
        return usuarios.stream()
                .map(listarDadosUsuarioMapper::toDTO)
                .toList();
    }


}
