package dev.bieelg18.Agendamento.repositories;

import dev.bieelg18.Agendamento.entities.Usuario;
import dev.bieelg18.Agendamento.enums.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByPermissao(Permissao permissao);

}
