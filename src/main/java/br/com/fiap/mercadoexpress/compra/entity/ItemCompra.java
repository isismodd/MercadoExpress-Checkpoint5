package br.com.fiap.mercadoexpress.compra.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "TDS_Itens_Compra")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_compra_seq")
    @SequenceGenerator(name = "item_compra_seq", sequenceName = "ITEM_COMPRA_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COMPRA_ID", nullable = false)
    private Compra compra;

    @Column(name = "PRODUTO_ID", nullable = false)
    private Long produtoId;

    @Column(name = "PRODUTO_NOME", nullable = false, length = 100)
    private String produtoNome;

    @Column(name = "QUANTIDADE", nullable = false)
    private Integer quantidade;

    @Column(name = "PRECO_UNITARIO", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    @Column(name = "CUSTO_UNITARIO", precision = 10, scale = 2)
    private BigDecimal custoUnitario;
}
