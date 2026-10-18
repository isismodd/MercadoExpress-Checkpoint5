package br.com.fiap.mercadoexpress.pagamento.dto;

import java.math.BigDecimal;

/** Corpo enviado para a API do Banco Tranquilo. */
public record BancoPagamentoRequest(
        Long compraId,
        String titular,
        String numeroCartao,
        BigDecimal valor
) {
}
