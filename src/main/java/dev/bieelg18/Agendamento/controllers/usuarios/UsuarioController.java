package dev.bieelg18.Agendamento.controllers.usuarios;

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
public class UsuarioController {

    private final UsuarioService usuarioService;

    //Rota para criar um usuário
    @PostMapping
    public ListarDadosUsuarioDTO criarUsuario(@RequestBody CriarUsuarioDTO criarDTO){
        return usuarioService.criarUsuario(criarDTO);
    }

    //Rota para listar todos os usuários
    @GetMapping("/all")
    public List<ListarDadosUsuarioDTO> todosOsUsuarios(){
        return usuarioService.listarUsuarios();
    }

    //Rota para buscar usuário por e-mail
    @GetMapping("/buscar")
    public ListarDadosUsuarioDTO buscarEmail(@RequestParam String email){
        return usuarioService.buscarEmail(email);
    }

    //Rota para alterar a permissão de um usuário
    @PatchMapping("/permissao/{id}")
    public ListarDadosUsuarioDTO permissao(@PathVariable Integer id, @RequestBody EditarPermissaoDTO permissaoDTO){
        return usuarioService.editarPermissao(id, permissaoDTO);
    }

    //Rota para alterar dados de cadastro do usuário
    @PatchMapping("/me")
    public ListarDadosUsuarioDTO editarDados(EditarDadosUsuarioDTO editarDTO, Authentication authentication){
        return usuarioService.editarDadosCadastro(editarDTO, authentication);
    }

    @DeleteMapping("/{id}")
    public void deletarUsuario(@PathVariable Integer id){
        usuarioService.deletarUsuario(id);
    }

    //Rota para listar usuários pela permissão
    @GetMapping
    public List<ListarDadosUsuarioDTO> listarPermissao(@RequestParam Permissao permissao){
        return usuarioService.listarPermissao(permissao);
    }

}
