package br.com.fiap.mercadoexpress.produto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProdutoRequest(

        @NotBlank(message = "nome é obrigatório")
        String nome,

        String tipo,

        String setor,

        String tamanho,

        @NotNull(message = "preco é obrigatório")
        @Positive(message = "preco deve ser maior que zero")
        BigDecimal preco,

        @PositiveOrZero(message = "custo não pode ser negativo")
        BigDecimal custo,

        @PositiveOrZero(message = "estoque não pode ser negativo")
        Integer estoque
) {
}
