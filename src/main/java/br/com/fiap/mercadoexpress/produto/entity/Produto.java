package br.com.fiap.mercadoexpress.produto.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "TDS_Sec_MVC_TB_Mercado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mercado_seq")
    @SequenceGenerator(name = "mercado_seq", sequenceName = "MERCADO_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @Column(name = "TIPO", length = 50)
    private String tipo;

    @Column(name = "SETOR", length = 50)
    private String setor;

    @Column(name = "TAMANHO", length = 20)
    private String tamanho;

    @Column(name = "PRECO", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(name = "CUSTO", precision = 10, scale = 2)
    private BigDecimal custo;

    @Column(name = "ESTOQUE")
    private Integer estoque;
}
