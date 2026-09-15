package dev.bieelg18.Agendamento.entities;

import dev.bieelg18.Agendamento.enums.DuracaoServico;
import dev.bieelg18.Agendamento.enums.TipoServico;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tb_servicos")
public class Servicos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoServico servico;

    @OneToMany(mappedBy = "preco")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DuracaoServico duracao;

    @OneToMany(mappedBy = "servico")
    private List<Agendamento> agendamento;

}
