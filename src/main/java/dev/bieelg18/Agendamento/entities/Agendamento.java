package dev.bieelg18.Agendamento.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tb_agendamentos")
public class Agendamento {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "idProfissional")
    private Usuario profissional;

    @ManyToOne
    @JoinColumn(name = "idServico")
    private Servicos servico;

    @Column(nullable = false)
    private LocalDateTime data;

}
