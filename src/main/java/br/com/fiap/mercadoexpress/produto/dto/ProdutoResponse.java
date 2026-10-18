package br.com.fiap.mercadoexpress.produto.dto;

import java.math.BigDecimal;

public record ProdutoResponse(
        Long id,
        String nome,
        String tipo,
        String setor,
        String tamanho,
        BigDecimal preco,
        BigDecimal custo,
        Integer estoque
) {
}
