package br.com.fiap.mercadoexpress.pagamento.dto;

import java.math.BigDecimal;

/** Mensagem publicada na fila de requisição de pagamento. */
public record PagamentoRequest(
        Long compraId,
        String clienteNome,
        String clienteEmail,
        BigDecimal valor,
        String numeroCartao
) {
}
