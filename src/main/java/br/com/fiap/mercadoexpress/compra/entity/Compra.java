package br.com.fiap.mercadoexpress.compra.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TDS_Compras_Mercado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "compra_seq")
    @SequenceGenerator(name = "compra_seq", sequenceName = "COMPRA_MERCADO_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CLIENTE_NOME", nullable = false, length = 100)
    private String clienteNome;

    @Column(name = "CLIENTE_EMAIL", nullable = false, length = 150)
    private String clienteEmail;

    @Column(name = "TOTAL", nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    /** PENDENTE, APROVADA ou RECUSADA */
    @Column(name = "STATUS", nullable = false, length = 20)
    private String status;

    @Column(name = "TRANSACAO_ID", length = 60)
    private String transacaoId;

    @Column(name = "MENSAGEM_BANCO", length = 300)
    private String mensagemBanco;

    @Column(name = "DATA_COMPRA", nullable = false)
    private LocalDateTime dataCompra;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<ItemCompra> itens = new ArrayList<>();
}
