package dev.bieelg18.Agendamento.controllers.usuarios;

import dev.bieelg18.Agendamento.docs.usuarios.UsuarioControllerDocs;
import dev.bieelg18.Agendamento.dtos.usuarios.CriarUsuarioDTO;
import dev.bieelg18.Agendamento.dtos.usuarios.EditarDadosUsuarioDTO;
import dev.bieelg18.Agendamento.dtos.usuarios.EditarPermissaoDTO;
import dev.bieelg18.Agendamento.dtos.usuarios.ListarDadosUsuarioDTO;
import dev.bieelg18.Agendamento.enums.Permissao;
import dev.bieelg18.Agendamento.services.usuarios.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController implements UsuarioControllerDocs {

    private final UsuarioService usuarioService;

    //Rota para criar um usuário
    @Override
    @PostMapping
    public ListarDadosUsuarioDTO criarUsuario(@RequestBody CriarUsuarioDTO criarDTO){
        return usuarioService.criarUsuario(criarDTO);
    }

    //Rota para listar todos os usuários
    @Override
    @GetMapping("/all")
    public List<ListarDadosUsuarioDTO> todosOsUsuarios(){
        return usuarioService.listarUsuarios();
    }

    //Rota para buscar usuário por e-mail
    @Override
    @GetMapping("/buscar")
    public ListarDadosUsuarioDTO buscarEmail(@RequestParam String email){
        return usuarioService.buscarEmail(email);
    }

    //Rota para alterar a permissão de um usuário
    @Override
    @PatchMapping("/permissao/{id}")
    public ListarDadosUsuarioDTO permissao(@PathVariable Integer id, @RequestBody EditarPermissaoDTO permissaoDTO){
        return usuarioService.editarPermissao(id, permissaoDTO);
    }

    //Rota para alterar dados de cadastro do usuário
    @Override
    @PatchMapping("/me")
    public ListarDadosUsuarioDTO editarDados(EditarDadosUsuarioDTO editarDTO, Authentication authentication){
        return usuarioService.editarDadosCadastro(editarDTO, authentication);
    }

    @Override
    @DeleteMapping("/{id}")
    public void deletarUsuario(@PathVariable Integer id){
        usuarioService.deletarUsuario(id);
    }

    //Rota para listar usuários pela permissão
    @Override
    @GetMapping
    public List<ListarDadosUsuarioDTO> listarPermissao(@RequestParam Permissao permissao){
        return usuarioService.listarPermissao(permissao);
    }

}
