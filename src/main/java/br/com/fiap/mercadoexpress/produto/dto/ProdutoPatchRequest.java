package br.com.fiap.mercadoexpress.produto.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProdutoPatchRequest(
        String nome,
        String tipo,
        String setor,
        String tamanho,

        @Positive(message = "preco deve ser maior que zero")
        BigDecimal preco,

        @PositiveOrZero(message = "custo não pode ser negativo")
        BigDecimal custo,

        @PositiveOrZero(message = "estoque não pode ser negativo")
        Integer estoque
) {
}
