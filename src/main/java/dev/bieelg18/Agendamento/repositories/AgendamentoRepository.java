package dev.bieelg18.Agendamento.repositories;

import dev.bieelg18.Agendamento.entities.Agendamento;
import dev.bieelg18.Agendamento.entities.Servicos;
import dev.bieelg18.Agendamento.entities.Usuario;
import dev.bieelg18.Agendamento.enums.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {

    List<Agendamento> findByStatus(StatusAgendamento status);

    List<Agendamento> findByProfissional(Usuario profissional);

    List<Agendamento> findByCliente(Usuario cliente);

}
